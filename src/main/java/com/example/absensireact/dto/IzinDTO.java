package com.example.absensireact.dto;

import com.example.absensireact.model.User;

import java.util.Date;

public class IzinDTO {
    private Long id;
    private Date tanggalIzin;
    private String keternganIzin;
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTanggalIzin() {
        return tanggalIzin;
    }

    public void setTanggalIzin(Date tanggalIzin) {
        this.tanggalIzin = tanggalIzin;
    }

    public String getKeternganIzin() {
        return keternganIzin;
    }

    public void setKeternganIzin(String keternganIzin) {
        this.keternganIzin = keternganIzin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
