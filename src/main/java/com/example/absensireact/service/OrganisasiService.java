package com.example.absensireact.service;

import com.example.absensireact.model.Organisasi;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface OrganisasiService {
    Organisasi GetAllOrganisasi();

    Optional<Organisasi> GetOrganisasiById(Long id);

    List<Organisasi> GetAllBYId(Long id);

    Organisasi GetAllByIdAdmin(Long idAdmin);

    Organisasi TambahOrganisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException;

    String uploadFoto(MultipartFile image) throws IOException;

    Organisasi UbahDataOrgannisasi(Long idAdmin, Organisasi organisasi, MultipartFile image);

    Organisasi EditByid(Long id, Organisasi organisasi, MultipartFile image) throws IOException;

    void deleteKaryawan(Long id);
}
