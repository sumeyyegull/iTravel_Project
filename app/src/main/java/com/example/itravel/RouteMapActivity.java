package com.example.itravel;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itravel.osrm.OsrmClient;
import com.example.itravel.osrm.OsrmGeoJsonParser;
import com.example.itravel.osrm.model.OsrmResponse;
import com.example.itravel.osrm.model.OsrmRoute;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "RouteMapActivity";

    public static final String EXTRA_DEST_LAT = "dest_lat";
    public static final String EXTRA_DEST_LON = "dest_lon";
    public static final String EXTRA_DEST_TITLE = "dest_title";

    private static final LatLng ISTANBUL_CENTER = new LatLng(41.0082, 28.9784);
    private static final float ISTANBUL_ZOOM = 11.5f;
    private static final int ROUTE_POLYLINE_COLOR = Color.parseColor("#14502E");
    private static final float ROUTE_POLYLINE_WIDTH = 12f;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng userLatLng;
    private LatLng destLatLng;
    private String destTitle;
    private Call<OsrmResponse> routeCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        destTitle = getIntent().getStringExtra(EXTRA_DEST_TITLE);
        if (destTitle == null) {
            destTitle = getString(R.string.route_destination);
        }

        double lat = getIntent().getDoubleExtra(EXTRA_DEST_LAT, Double.NaN);
        double lon = getIntent().getDoubleExtra(EXTRA_DEST_LON, Double.NaN);
        if (Double.isNaN(lat) || Double.isNaN(lon)) {
            Toast.makeText(this, R.string.route_invalid_destination, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        destLatLng = new LatLng(lat, lon);

        MaterialToolbar toolbar = findViewById(R.id.route_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(destTitle);

        TextView titleView = findViewById(R.id.route_dest_title);
        titleView.setText(destTitle);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationAndMap();
    }

    @Override
    protected void onDestroy() {
        if (routeCall != null) {
            routeCall.cancel();
        }
        super.onDestroy();
    }

    private void requestLocationAndMap() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        initMapFragment();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(RouteMapActivity.this, R.string.route_location_denied, Toast.LENGTH_LONG).show();
                        initMapFragment();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest request, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void initMapFragment() {
        setLoading(true);
        SupportMapFragment fragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.route_map_container, fragment)
                .commit();
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ISTANBUL_CENTER, ISTANBUL_ZOOM));

        applyMapInsets();

        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException ignored) {
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    } else {
                        userLatLng = ISTANBUL_CENTER;
                    }
                    drawRoute();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get last location", e);
                    userLatLng = ISTANBUL_CENTER;
                    drawRoute();
                });
    }

    private void applyMapInsets() {
        if (googleMap == null) {
            return;
        }
        MaterialCardView panel = findViewById(R.id.route_bottom_panel);
        View toolbar = findViewById(R.id.route_toolbar);
        panel.post(() -> {
            int top = toolbar.getHeight();
            int bottom = panel.getHeight() + dp(20);
            googleMap.setPadding(0, top, 0, bottom);
        });
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void drawRoute() {
        if (googleMap == null || userLatLng == null || destLatLng == null) {
            setLoading(false);
            return;
        }

        googleMap.clear();

        googleMap.addMarker(new MarkerOptions()
                .position(userLatLng)
                .title(getString(R.string.route_you))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        googleMap.addMarker(new MarkerOptions()
                .position(destLatLng)
                .title(destTitle)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        fetchOsrmRoute();
    }

    private void fetchOsrmRoute() {
        setLoading(true);

        String coordinates = String.format(Locale.US, "%f,%f;%f,%f",
                userLatLng.longitude, userLatLng.latitude,
                destLatLng.longitude, destLatLng.latitude);

        if (routeCall != null) {
            routeCall.cancel();
        }

        Log.d(TAG, "Requesting OSRM route: " + coordinates);

        routeCall = OsrmClient.getService()
                .getRoute(coordinates, "full", "geojson");

        routeCall.enqueue(new Callback<OsrmResponse>() {
            @Override
            public void onResponse(@NonNull Call<OsrmResponse> call,
                                   @NonNull Response<OsrmResponse> response) {
                setLoading(false);
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e(TAG, "OSRM HTTP error: code=" + response.code()
                            + " message=" + response.message());
                    onRouteFailed("HTTP " + response.code());
                    return;
                }

                OsrmResponse body = response.body();
                if (!"Ok".equalsIgnoreCase(body.code)
                        || body.routes == null
                        || body.routes.isEmpty()) {
                    String apiMessage = body.message != null ? body.message : "unknown";
                    Log.e(TAG, "OSRM code=" + body.code + " message=" + apiMessage);
                    onRouteFailed(body.code);
                    return;
                }

                Log.d(TAG, "OSRM route received, drawing polyline");
                applyOsrmRoute(body.routes.get(0));
            }

            @Override
            public void onFailure(@NonNull Call<OsrmResponse> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    return;
                }
                setLoading(false);
                Log.e(TAG, "OSRM network failure", t);
                onRouteFailed(t.getMessage());
            }
        });
    }

    private void applyOsrmRoute(OsrmRoute route) {
        if (route.geometry == null) {
            Log.e(TAG, "OSRM route missing geometry");
            onRouteFailed("missing geometry");
            return;
        }

        List<LatLng> path = OsrmGeoJsonParser.toLatLngPath(route.geometry);
        if (path.isEmpty()) {
            Log.e(TAG, "OSRM GeoJSON coordinates are empty");
            onRouteFailed("empty coordinates");
            return;
        }

        double distanceKm = route.distance / 1000.0;
        int minutes = (int) Math.max(1, Math.round(route.duration / 60.0));

        TextView distanceTv = findViewById(R.id.route_distance);
        TextView durationTv = findViewById(R.id.route_duration);
        distanceTv.setText(getString(R.string.route_distance, distanceKm));
        durationTv.setText(getString(R.string.route_duration, minutes));

        Log.d(TAG, "Polyline points=" + path.size()
                + " distanceKm=" + distanceKm
                + " durationMin=" + minutes);

        googleMap.addPolyline(new PolylineOptions()
                .addAll(path)
                .width(ROUTE_POLYLINE_WIDTH)
                .color(ROUTE_POLYLINE_COLOR));

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng point : path) {
            boundsBuilder.include(point);
        }
        fitCamera(boundsBuilder.build());
    }

    private void onRouteFailed(String reason) {
        Log.w(TAG, "Showing map without route polyline. reason=" + reason);
        Toast.makeText(this, R.string.route_directions_error, Toast.LENGTH_SHORT).show();
        fitCameraToEndpoints();
    }

    private void fitCameraToEndpoints() {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(userLatLng)
                .include(destLatLng)
                .build();
        fitCamera(bounds);
    }

    private void fitCamera(LatLngBounds bounds) {
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, dp(48)));
        } catch (Exception e) {
            Log.w(TAG, "LatLngBounds camera failed, using default zoom", e);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ISTANBUL_CENTER, ISTANBUL_ZOOM));
        }
    }

    private void setLoading(boolean loading) {
        ProgressBar bar = findViewById(R.id.route_progress);
        if (bar != null) {
            bar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
    }
}
