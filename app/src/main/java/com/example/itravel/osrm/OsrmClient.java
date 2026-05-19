package com.example.itravel.osrm;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class OsrmClient {

    private static final String BASE_URL = "https://router.project-osrm.org/";

    private static OsrmApiService service;

    private OsrmClient() {
    }

    public static OsrmApiService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(OsrmApiService.class);
        }
        return service;
    }
}
