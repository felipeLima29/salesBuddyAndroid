package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.model.request.LoginRequest;
import com.example.salesbuddy.model.request.LoginResponse;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextInputLayout txInputUser, txInputPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin = findViewById(R.id.btnLogin);
        txInputUser = findViewById(R.id.txInputUser);
        txInputPassword = findViewById(R.id.txInputPassword);

        SharedPreferencesUtil sp = SharedPreferencesUtil.instance(getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = Objects.requireNonNull(txInputUser.getEditText()).getText().toString().trim();
                String password = Objects.requireNonNull(txInputPassword.getEditText()).getText().toString().trim();

                if (usuario.isEmpty() || password.isEmpty()) {
                    ShowCustomToast.show(getApplicationContext(), "Preencha todos os campos", "WARNING");
                } else {
                    LoginRequest loginData = new LoginRequest(usuario, password);
                    ApiService api = RetrofitClient.createService(ApiService.class, getApplicationContext());
                    Call<LoginResponse> responseCall = api.login(loginData);

                    responseCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse loginResponse = response.body();
                            if (response.isSuccessful()) {
                                sp.storeValueString(StaticsKeysUtil.Token, loginResponse.getToken());
                                if (loginResponse.getLogin()) {
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    ShowCustomToast.show(getApplicationContext(), "Email e/ou senha inválidas.", "ERROR");
                                }
                            } else {
                                Log.e("ERROR", "sem sucesso: " + response.code());
                                try {
                                    Gson gson = new Gson();
                                    LoginResponse errorData = gson.fromJson(response.errorBody().charStream(), LoginResponse.class);

                                    String message = errorData.getMessage();
                                    ShowCustomToast.show(getApplicationContext(), message, "ERROR");
                                } catch (Exception e) {
                                    ShowCustomToast.show(getApplicationContext(), "Erro no servidor", "ERROR");
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e("ERROR", "onFailue: " + t.getMessage());
                        }
                    });
                }
            }
        });

    }
}