package com.example.itravel.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.itravel.Model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Turkish-aware place name search for category listing screens.
 */
public final class PlaceSearchUtils {

    private static final Locale TR = new Locale("tr", "TR");

    private PlaceSearchUtils() {
    }

    @NonNull
    public static String normalizeForSearch(@Nullable String text) {
        if (text == null) {
            return "";
        }
        String normalized = text.toLowerCase(TR).trim();
        normalized = normalized
                .replace('ç', 'c')
                .replace('ğ', 'g')
                .replace('ı', 'i')
                .replace('ö', 'o')
                .replace('ş', 's')
                .replace('ü', 'u');
        return normalized;
    }

    public static boolean matchesPlaceName(@NonNull Place place, @Nullable String query) {
        String normalizedQuery = normalizeForSearch(query);
        if (normalizedQuery.isEmpty()) {
            return true;
        }
        String title = place.getTitle();
        String normalizedTitle = normalizeForSearch(title);
        return normalizedTitle.contains(normalizedQuery);
    }

    @NonNull
    public static List<Place> filterByName(@NonNull List<Place> places, @Nullable String query) {
        String normalizedQuery = normalizeForSearch(query);
        if (normalizedQuery.isEmpty()) {
            return new ArrayList<>(places);
        }
        List<Place> out = new ArrayList<>();
        for (Place place : places) {
            if (matchesPlaceName(place, query)) {
                out.add(place);
            }
        }
        return out;
    }
}
