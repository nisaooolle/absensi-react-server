package com.example.absensireact.service;

import com.example.absensireact.dto.AdminDTO;
import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.dto.OrganisasiDTO;
import com.example.absensireact.model.Lokasi;

import java.util.List;

public interface LokasiService {


    LokasiDTO saveLokasi(LokasiDTO lokasiDTO);

    List<Lokasi>getAllByAdmin(Long idAdmin);

    Lokasi tambahLokasi(Long idAdmin, Lokasi lokasi, Long idOrganisasi);

    List<LokasiDTO> getAllLokasi();

    LokasiDTO getLokasiById(Long id);

    LokasiDTO updateLokasi(Long id, LokasiDTO lokasiDTO);

    boolean deleteLokasi(Long id);

    OrganisasiDTO getOrganisasiById(Long id);

    AdminDTO getAdminById(Long id);
}

