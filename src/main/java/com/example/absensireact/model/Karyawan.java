package com.example.absensireact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "karyawan")
public class Karyawan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jabatan")
    private String jabatan;

    @Column(name = "shift")
    private String shift;

    @Column(name = "fotoKaryawan")
    private String fotoKaryawan;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public Karyawan(){

    }

    public Karyawan(Long id, String jabatan, String shift,String fotoKaryawan ,User user) {
        this.id = id;
        this.jabatan = jabatan;
        this.shift = shift;
        this.fotoKaryawan = fotoKaryawan;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFotoKaryawan() {
        return fotoKaryawan;
    }

    public void setFotoKaryawan(String fotoKaryawan) {
        this.fotoKaryawan = fotoKaryawan;
    }
}
