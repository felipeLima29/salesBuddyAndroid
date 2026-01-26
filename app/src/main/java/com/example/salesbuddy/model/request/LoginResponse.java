package com.example.salesbuddy.model.request;

public class LoginResponse {
    private boolean error;
    private boolean login;
    private String token;
    private int id;
    private String message;

    public LoginResponse(boolean error, boolean login, String token, int id, String message) {
        this.error = error;
        this.login = login;
        this.token = token;
        this.id = id;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
