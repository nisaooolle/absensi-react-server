package com.example.absensireact.dto;

import com.example.absensireact.model.Organisasi;

public class AdminDTO {
    private Long id;
    private String email;
    private String password;
    private String username;
    private String imageAdmin;
    private String idOrganisasi;

    public AdminDTO(Long id, String email, String password, String username, String imageAdmin, Organisasi organisasi) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.imageAdmin = imageAdmin;
        // Ambil ID organisasi dan konversi menjadi String
        this.idOrganisasi = String.valueOf(organisasi.getId());
    }

    public AdminDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getIdOrganisasi() {
        return idOrganisasi;
    }

    public void setIdOrganisasi(String idOrganisasi) {
        this.idOrganisasi = idOrganisasi;
    }
}
