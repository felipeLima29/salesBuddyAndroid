package com.example.salesbuddy.model.api;

import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.request.Sales;
import com.example.salesbuddy.model.request.SalesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/listAllSales")
    Call<List<Sales>> getSales();
    @POST("/insertSale")
    Call<SalesResponse> insertSale(@Body SaleSerializable sales);

}
