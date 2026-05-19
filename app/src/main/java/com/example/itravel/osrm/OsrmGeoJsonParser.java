package com.example.itravel.osrm;

import com.example.itravel.osrm.model.OsrmGeometry;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public final class OsrmGeoJsonParser {

    private OsrmGeoJsonParser() {
    }

    public static List<LatLng> toLatLngPath(OsrmGeometry geometry) {
        List<LatLng> path = new ArrayList<>();
        if (geometry == null || geometry.coordinates == null) {
            return path;
        }

        for (List<Double> coordinate : geometry.coordinates) {
            if (coordinate == null || coordinate.size() < 2) {
                continue;
            }
            double lon = coordinate.get(0);
            double lat = coordinate.get(1);
            path.add(new LatLng(lat, lon));
        }
        return path;
    }
}
