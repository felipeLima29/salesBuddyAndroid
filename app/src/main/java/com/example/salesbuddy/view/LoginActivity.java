package com.example.salesbuddy.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.salesbuddy.R;
import com.example.salesbuddy.controller.LoginController;
import com.example.salesbuddy.utils.ShowCustomToast;
import com.example.salesbuddy.view.contracts.ILoginView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private Button btnLogin;
    private TextInputLayout txInputUser, txInputPassword;

    private LoginController controller;

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

        controller = new LoginController(this, this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = Objects.requireNonNull(txInputUser.getEditText().getText().toString().trim());
                String password = Objects.requireNonNull(txInputPassword.getEditText().getText().toString().trim());
                controller.doLogin(usuario, password);
            }
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
    public void onLoginSuccess() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(String message) {
        ShowCustomToast.show(getApplicationContext(), message, "ERROR");
    }
}