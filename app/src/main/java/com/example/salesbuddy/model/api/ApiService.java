package com.example.salesbuddy.model.api;

import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.request.LoginRequest;
import com.example.salesbuddy.model.request.LoginResponse;
import com.example.salesbuddy.model.request.Sales;
import com.example.salesbuddy.model.request.SalesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @GET("/listAllSales")
    Call<List<Sales>> getSales();
    @POST("/insertSale")
    Call<SalesResponse> insertSale(@Body SaleSerializable sales, @Header("Authorization") String token);
}
