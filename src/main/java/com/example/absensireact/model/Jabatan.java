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
    @JoinColumn(name = "admin_id") // Changed to reference an admin
    private Admin admin;

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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
