package com.example.itravel.osrm;

import com.example.itravel.osrm.model.OsrmResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OsrmApiService {

    @GET("route/v1/driving/{coordinates}")
    Call<OsrmResponse> getRoute(
            @Path(value = "coordinates", encoded = true) String coordinates,
            @Query("overview") String overview,
            @Query("geometries") String geometries
    );
}
