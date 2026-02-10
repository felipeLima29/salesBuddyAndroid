package com.example.salesbuddy.model.api;

import com.example.salesbuddy.model.SaleSerializable;
import com.example.salesbuddy.model.request.LoginRequest;
import com.example.salesbuddy.model.request.LoginResponse;
import com.example.salesbuddy.model.request.SalesResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("/insertSale")
    Call<SalesResponse> insertSale(@Body SaleSerializable sales, @Header("Authorization") String token);

    @Multipart
    @POST("/sendReceipt")
    Call<ResponseBody> sendReceipt(
            @Part MultipartBody.Part image,
            @Query("email") String email,
            @Header("Authorization") String token
    );
}
