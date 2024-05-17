package com.example.absensireact.service;

import com.example.absensireact.dto.AdminDTO;
import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.dto.OrganisasiDTO;

import java.util.List;

public interface LokasiService {

    LokasiDTO saveLokasi(LokasiDTO lokasiDTO);

    List<LokasiDTO> getAllLokasi();

    LokasiDTO getLokasiById(Integer id);

    LokasiDTO updateLokasi(Integer id, LokasiDTO lokasiDTO);

    boolean deleteLokasi(Integer id);
}

