package com.mobisquid.mobicash.ecommerce.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zeera on 6/28/2017 bt ${File}
 */

public class ApiClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitFiniacial = null;

    public static final String BASE_URL_ECOMMERCE = "http://52.38.75.235:8080";
    public static final String BASE_URL_FINIACIAL = "http://test.mcash.rw";

    final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder ongoing = chain.request().newBuilder();
                    ongoing.addHeader("Accept", "application/json;versions=1");
                    if (/*isUserLoggedIn()*/true) {
                        ongoing.addHeader("Authorization", " Basic Q1BBUEkwMDE6QVBJQ1BBdXRoMDAx");
                    }
                    return chain.proceed(ongoing.build());
                }
            })
            .build();

    private static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_ECOMMERCE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitFiniacial() {
        if (retrofitFiniacial == null) {
            retrofitFiniacial = new Retrofit.Builder()
                    .baseUrl(BASE_URL_FINIACIAL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitFiniacial;
    }


    public static IApiInterface getApiClient() {
        return getClient().create(IApiInterface.class);
    }
}
