package com.example.absensireact.service;

import com.example.absensireact.model.Organisasi;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface OrganisasiService {
    Organisasi GetAllOrganisasi();

    Optional<Organisasi> GetOrganisasiById(Long id);

    Optional<Organisasi> GetAllBYId(Long id);

    Optional<Organisasi> GetAllByIdAdmin(Long idAdmin);

    Organisasi TambahOrganisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException;

    String uploadFoto(MultipartFile image) throws IOException;

    Organisasi UbahDataOrgannisasi(Long idAdmin, Organisasi organisasi, MultipartFile image);

    Organisasi EditByid(Long id, Organisasi organisasi, MultipartFile image) throws IOException;

    void deleteKaryawan(Long id);
}
