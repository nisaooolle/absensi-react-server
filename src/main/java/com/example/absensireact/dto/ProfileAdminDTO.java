package com.example.absensireact.dto;

public class ProfileAdminDTO {
    private String email;
    private String username;
    private String imageAdmin; // Add this field to handle image path

    public ProfileAdminDTO(String email, String username, String imageAdmin) {
        this.email = email;
        this.username = username;
        this.imageAdmin = imageAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageAdmin() {
        return imageAdmin;
    }

    public void setImageAdmin(String imageAdmin) {
        this.imageAdmin = imageAdmin;
    }
}
