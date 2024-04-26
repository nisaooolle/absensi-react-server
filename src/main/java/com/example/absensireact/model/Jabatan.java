package com.example.absensireact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "jabatan")
public class Jabatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJabatan;

    @Column(name = "nama_jabatan")
    private String namaJabatan;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id") // Changed from id_admin to user_id
    private User admin;

    public Long getIdJabatan() {
        return idJabatan;
    }

    public void setIdJabatan(Long idJabatan) {
        this.idJabatan = idJabatan;
    }

    public String getNamaJabatan() {
        return namaJabatan;
    }

    public void setNamaJabatan(String namaJabatan) {
        this.namaJabatan = namaJabatan;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
