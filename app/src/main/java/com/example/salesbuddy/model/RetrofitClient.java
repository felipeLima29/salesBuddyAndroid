package com.example.salesbuddy.model;

import android.content.Context;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    Context context;

    public RetrofitClient(Context context) {
        this.context = context;
    }

    private static final String baseUrl = "http://192.168.124.109:3000";
    static Retrofit retrofit;
    public static <S> S createService(Class<S> sClass, Context context) {

        HttpLoggingInterceptor interceptorHTTP = new HttpLoggingInterceptor();
        interceptorHTTP.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptorHTTP).readTimeout(10, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(client.build())
                .build();

        return retrofit.create(sClass);
    }
}
