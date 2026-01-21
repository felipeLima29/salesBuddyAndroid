package com.example.salesbuddy.model.api;

import com.example.salesbuddy.model.Sales;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/listAllSales")
    Call<List<Sales>> getSales();

}
