package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.RetrofitClient;
import com.example.salesbuddy.model.api.ApiService;
import com.example.salesbuddy.presenter.LoginPresenter;
import com.example.salesbuddy.utils.SharedPreferencesUtil;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.utils.StaticsKeysUtil;
import com.example.salesbuddy.view.contracts.ILoginView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private Button btnLogin;
    private TextInputLayout txInputUser, txInputPassword;

    private LoginPresenter presenter;

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

        ApiService apiService = RetrofitClient.createService(ApiService.class, this);
        presenter = new LoginPresenter(this, apiService);

        btnLogin.setOnClickListener(v -> {
            String usuario = Objects.requireNonNull(txInputUser.getEditText().getText().toString().trim());
            String password = Objects.requireNonNull(txInputPassword.getEditText().getText().toString().trim());
            presenter.doLogin(usuario, password);
        });
    }


    @Override
    public void showLoading() {
        btnLogin.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void onLoginSuccess(String token) {
        SharedPreferencesUtil sp = SharedPreferencesUtil.instance(getApplicationContext());
        sp.storeValueString(StaticsKeysUtil.Token, token);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String message) {
        ShowCustomToast.show(getApplicationContext(), message, "ERROR");
    }

    @Override
    public void onEmptyFieldsError() {
        String msg = getString(R.string.fields_null);
        ShowCustomToast.show(getApplicationContext(), msg, "ERROR");
    }

    @Override
    public void onConnectionError() {
        String msg = getString(R.string.error_conex);
        ShowCustomToast.show(this, msg, "ERROR");
    }
}