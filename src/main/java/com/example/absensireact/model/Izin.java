package com.example.absensireact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "izin")
public class Izin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tanggalIzin")
    private LocalDate tanggalIzin;

    @Column(name = "keteranganIzin")
    private String keternganIzin;


     @OneToOne
    @JoinColumn(name = "userId")
    private  User user;

    public Izin (){}

    public Izin(Long id, LocalDate tanggalIzin, String keternganIzin, User user) {
        this.id = id;
        this.tanggalIzin = tanggalIzin;
        this.keternganIzin = keternganIzin;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTanggalIzin() {
        return tanggalIzin;
    }

    public void setTanggalIzin(LocalDate tanggalIzin) {
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
