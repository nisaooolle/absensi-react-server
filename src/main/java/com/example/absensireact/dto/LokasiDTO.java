package com.example.absensireact.dto;

public class LokasiDTO {
    private String namaLokasi;
    private String alamat;
    private Long idOrganisasi;
    private Long idAdmin;

    // Constructors
    public LokasiDTO() {}

    // Typically, you might include a constructor for convenience to transform from entity to DTO
    public LokasiDTO(String namaLokasi, String alamat, Long idOrganisasi, Long idAdmin) {
        this.namaLokasi = namaLokasi;
        this.alamat = alamat;
        this.idOrganisasi = idOrganisasi;
        this.idAdmin = idAdmin;
    }

    // Getters and setters
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
