package com.example.pasantias.appcamaleon.Pojos;

public class Usuario {

    String user;
    String password;
    String token;


    public Usuario() {
    }

    public Usuario(String user, String password, String token) {
        this.user = user;
        this.password = password;
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
