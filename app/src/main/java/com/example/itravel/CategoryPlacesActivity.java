package com.example.itravel;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itravel.Adapter.PlaceAdapter;
import com.example.itravel.Model.Place;
import com.example.itravel.Model.PlaceCategory;
import com.example.itravel.ui.PlaceSearchBarHelper;
import com.example.itravel.util.PlaceFilter;
import com.example.itravel.util.PlaceSearchUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryPlacesActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "category";

    private String categoryKey;
    private DatabaseReference placesRef;
    private ValueEventListener placesListener;
    private PlaceAdapter adapter;
    private TextView emptyView;
    private final List<Place> categoryPlaces = new ArrayList<>();
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_places);

        categoryKey = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (!PlaceCategory.isValid(categoryKey)) {
            finish();
            return;
        }

        MaterialToolbar toolbar = findViewById(R.id.category_toolbar);
        toolbar.setTitle(PlaceCategory.labelRes(categoryKey));
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_map) {
                MapActivity.launch(this, categoryKey);
                overridePendingTransition(R.anim.nav_fade_in, R.anim.nav_fade_out);
                return true;
            }
            return false;
        });

        SearchView searchView = findViewById(R.id.search_places);
        PlaceSearchBarHelper.bind(searchView, query -> {
            searchQuery = query;
            applySearchAndUpdateUi();
        });

        RecyclerView rv = findViewById(R.id.rv_places);
        emptyView = findViewById(R.id.empty_places);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter();
        rv.setAdapter(adapter);

        placesRef = FirebaseDatabase.getInstance(ItravelApp.FIREBASE_RTDB_URL)
                .getReference(ItravelApp.RTDB_NODE_PLACES);

        placesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Place> all = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Place p = Place.fromSnapshot(child);
                    if (p != null && p.getId() != null && !p.getId().isEmpty()) {
                        all.add(p);
                    }
                }
                categoryPlaces.clear();
                categoryPlaces.addAll(PlaceFilter.byCategory(all, categoryKey));
                applySearchAndUpdateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryPlaces.clear();
                applySearchAndUpdateUi();
            }
        };
        placesRef.addValueEventListener(placesListener);
    }

    private void applySearchAndUpdateUi() {
        List<Place> visible = PlaceSearchUtils.filterByName(categoryPlaces, searchQuery);
        adapter.submitList(visible);
        updateEmptyState(visible);
    }

    private void updateEmptyState(@NonNull List<Place> visible) {
        if (visible.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(searchQuery.trim())) {
                emptyView.setText(R.string.places_search_no_results);
            } else {
                emptyView.setText(R.string.places_empty_category);
            }
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (placesRef != null && placesListener != null) {
            placesRef.removeEventListener(placesListener);
        }
    }
}
