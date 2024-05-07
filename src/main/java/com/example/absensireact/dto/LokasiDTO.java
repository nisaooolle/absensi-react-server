package com.example.absensireact.dto;

public class LokasiDTO {
    private Long idLokasi;
    private String namaLokasi;
    private String alamat;
    private Long idOrganisasi; // Menggunakan Long
    private Long idAdmin; // Menggunakan Long

    // Constructors
    public LokasiDTO() {}

    // Typically, you might include a constructor for convenience to transform from entity to DTO
    public LokasiDTO(Long idLokasi, String namaLokasi, String alamat, Long idOrganisasi, Long idAdmin) {
        this.idLokasi = idLokasi;
        this.namaLokasi = namaLokasi;
        this.alamat = alamat;
        this.idOrganisasi = idOrganisasi;
        this.idAdmin = idAdmin;
    }

    // Getters and setters
    public Long getIdLokasi() {
        return idLokasi;
    }

    public void setIdLokasi(Long idLokasi) {
        this.idLokasi = idLokasi;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Long getIdOrganisasi() {
        return idOrganisasi;
    }

    public void setIdOrganisasi(Long idOrganisasi) {
        this.idOrganisasi = idOrganisasi;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }
}
