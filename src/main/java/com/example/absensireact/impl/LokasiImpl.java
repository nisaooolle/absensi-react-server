package com.example.absensireact.impl;

import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Lokasi;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.LokasiRepository;
import com.example.absensireact.repository.OrganisasiRepository;
import com.example.absensireact.service.LokasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LokasiImpl implements LokasiService {

    @Autowired
    private LokasiRepository lokasiRepository;

    @Autowired
    private OrganisasiRepository organisasiRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public LokasiDTO saveLokasi(LokasiDTO lokasiDTO) {
        Lokasi lokasi = convertToEntity(lokasiDTO);
        lokasi = lokasiRepository.save(lokasi);
        return convertToDto(lokasi);
    }

    @Override
    public List<LokasiDTO> getAllLokasi() {
        return lokasiRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LokasiDTO getLokasiById(Integer id) {
        Optional<Lokasi> lokasi = lokasiRepository.findById(id);
        return lokasi.map(this::convertToDto).orElse(null);
    }

    @Override
    public LokasiDTO updateLokasi(Integer id, LokasiDTO lokasiDTO) {
        return lokasiRepository.findById(id).map(existingLokasi -> {
            updateEntity(existingLokasi, lokasiDTO);
            lokasiRepository.save(existingLokasi);
            return convertToDto(existingLokasi);
        }).orElse(null);
    }

    @Override
    public boolean deleteLokasi(Integer id) {
        return lokasiRepository.findById(id).map(lokasi -> {
            lokasiRepository.delete(lokasi);
            return true;
        }).orElse(false);
    }

    @Override
    public Organisasi getOrganisasiByLokasiId(Integer id) {
        return lokasiRepository.findById(id)
                .map(Lokasi::getOrganisasi)
                .orElse(null);
    }

    @Override
    public Admin getAdminByLokasiId(Integer id) {
        return lokasiRepository.findById(id)
                .map(Lokasi::getAdmin)
                .orElse(null);
    }



    private Lokasi convertToEntity(LokasiDTO lokasiDTO) {
        Lokasi lokasi = new Lokasi();
        lokasi.setNamaLokasi(lokasiDTO.getNamaLokasi());
        lokasi.setAlamat(lokasiDTO.getAlamat());

        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(lokasiDTO.getIdOrganisasi());
        organisasiOptional.ifPresent(lokasi::setOrganisasi);

        Optional<Admin> adminOptional = adminRepository.findById(lokasiDTO.getIdAdmin());
        adminOptional.ifPresent(lokasi::setAdmin);

        return lokasi;
    }

    private void updateEntity(Lokasi existingLokasi, LokasiDTO lokasiDTO) {
        existingLokasi.setNamaLokasi(lokasiDTO.getNamaLokasi());
        existingLokasi.setAlamat(lokasiDTO.getAlamat());

        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(lokasiDTO.getIdOrganisasi());
        organisasiOptional.ifPresent(existingLokasi::setOrganisasi);

        Optional<Admin> adminOptional = adminRepository.findById(lokasiDTO.getIdAdmin());
        adminOptional.ifPresent(existingLokasi::setAdmin);
    }

    private LokasiDTO convertToDto(Lokasi lokasi) {
        LokasiDTO lokasiDTO = new LokasiDTO();
        lokasiDTO.setNamaLokasi(lokasi.getNamaLokasi());
        lokasiDTO.setAlamat(lokasi.getAlamat());
        if (lokasi.getOrganisasi() != null) {
            lokasiDTO.setIdOrganisasi(lokasi.getOrganisasi().getId().longValue());
        }
        if (lokasi.getAdmin() != null) {
            lokasiDTO.setIdAdmin(lokasi.getAdmin().getId().longValue());
        }
        return lokasiDTO;
    }

}
