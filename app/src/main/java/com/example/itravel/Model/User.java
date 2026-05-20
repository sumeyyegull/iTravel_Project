package com.example.itravel.Model;

public class User {

    public String username, email, image_url;

    public User() {
        // Firebase için zorunlu
    }

    public User(String username, String email, String image_url) {
        this.username = username;
        this.email = email;
        this.image_url = image_url;
    }
}