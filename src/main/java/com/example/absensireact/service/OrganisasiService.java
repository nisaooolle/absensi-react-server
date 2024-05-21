package com.example.absensireact.service;

import com.example.absensireact.model.Organisasi;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface OrganisasiService {

    List<Organisasi> getAllOrganisasi();

    List<Organisasi>getAllByAdmin(Long idAdmin);

    Optional<Organisasi> GetOrganisasiById(Long id);
    Optional<Organisasi> GetAllBYId(Long idA);

    Optional<Organisasi> GetByIdAdmin(Long idAdmin);

    Organisasi TambahOrganisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException;


    Organisasi UbahDataOrgannisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException;

    Organisasi EditByid(Long id, Organisasi organisasi, MultipartFile image) throws IOException;

    void deleteOrganisasi(Long id) throws IOException;
}
