package com.example.absensireact.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "absensi")
public class Absensi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tanggalAbsen")
    private Date tanggalAbsen;

    @Column(name = "jamMasuk")
    private String jamMasuk;

    @Column(name = "jamPulang")
    private String jamPulang;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "fotoMasuk")
    private String fotoMasuk;

    @Column(name = "fotoPulang")
    private String fotoPulang;

    @Column(name = "status")
    private String status;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private User  user;


    public Absensi() {

    }

    public Absensi(Long id, Date tanggalAbsen, String jamMasuk, String jamPulang, String keterangan, String status ,String fotoMasuk , String fotoPulang, User user) {
        this.id = id;
        this.tanggalAbsen = tanggalAbsen;
        this.jamMasuk = jamMasuk;
        this.jamPulang = jamPulang;
        this.fotoPulang = fotoPulang;
        this.fotoMasuk = fotoMasuk;
        this.keterangan = keterangan;
        this.status = status;
        this.user = user;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTanggalAbsen() {
        return tanggalAbsen;
    }

    public void setTanggalAbsen(Date tanggalAbsen) {
        this.tanggalAbsen = tanggalAbsen;
    }

    public String getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(String jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public String getJamPulang() {
        return jamPulang;
    }

    public void setJamPulang(String jamPulang) {
        this.jamPulang = jamPulang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getFotoMasuk() {
        return fotoMasuk;
    }

    public void setFotoMasuk(String fotoMasuk) {
        this.fotoMasuk = fotoMasuk;
    }

    public String getFotoPulang() {
        return fotoPulang;
    }

    public void setFotoPulang(String fotoPulang) {
        this.fotoPulang = fotoPulang;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
