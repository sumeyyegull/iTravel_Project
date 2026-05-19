package com.example.itravel.ui;

import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.example.itravel.R;

/**
 * Reusable SearchView setup for category place listing screens.
 */
public final class PlaceSearchBarHelper {

    public interface QueryListener {
        void onQueryChanged(@NonNull String query);
    }

    private PlaceSearchBarHelper() {
    }

    public static void bind(@NonNull SearchView searchView, @NonNull QueryListener listener) {
        int green = ContextCompat.getColor(searchView.getContext(), R.color.green);
        int muted = ContextCompat.getColor(searchView.getContext(), R.color.role_text_muted);

        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();

        AutoCompleteTextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchText != null) {
            searchText.setTextColor(green);
            searchText.setHintTextColor(muted);
            searchText.setTextSize(16f);
        }

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        if (searchIcon != null) {
            searchIcon.setColorFilter(green);
        }

        ImageView closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        if (closeIcon != null) {
            closeIcon.setColorFilter(muted);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listener.onQueryChanged(newText != null ? newText : "");
                return true;
            }
        });
    }
}
