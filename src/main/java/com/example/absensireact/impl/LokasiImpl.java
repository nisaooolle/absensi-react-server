package com.example.absensireact.impl;

import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.dto.AdminDTO;
import com.example.absensireact.dto.OrganisasiDTO;
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

        // Ambil entitas Organisasi dari basis data berdasarkan ID yang diberikan
        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(lokasiDTO.getIdOrganisasi());
        organisasiOptional.ifPresent(lokasi::setOrganisasi);

        // Jika ada adminId yang diberikan, ambil entitas Admin dari basis data
        if (lokasiDTO.getAdminId() != null) {
            Optional<Admin> adminOptional = adminRepository.findById(lokasiDTO.getAdminId());
            adminOptional.ifPresent(lokasi::setAdmin);
        }

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

    private Lokasi convertToEntity(LokasiDTO lokasiDTO) {
        Lokasi lokasi = new Lokasi();
        lokasi.setNamaLokasi(lokasiDTO.getNamaLokasi());
        lokasi.setAlamat(lokasiDTO.getAlamat());

        return lokasi;
    }


    private AdminDTO convertAdminToDto(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setPassword(admin.getPassword());
        adminDTO.setUsername(admin.getUsername());
        adminDTO.setImageAdmin(admin.getImageAdmin());

        // Set ID Organisasi
        Organisasi organisasi = admin.getOrganisasi();
        if (organisasi != null) {
            adminDTO.setIdOrganisasi(String.valueOf(organisasi.getId()));
        }

        return adminDTO;
    }



    private void updateEntity(Lokasi existingLokasi, LokasiDTO lokasiDTO) {
        existingLokasi.setNamaLokasi(lokasiDTO.getNamaLokasi());
        existingLokasi.setAlamat(lokasiDTO.getAlamat());

        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(lokasiDTO.getIdOrganisasi());
        organisasiOptional.ifPresent(existingLokasi::setOrganisasi);
    }

    private LokasiDTO convertToDto(Lokasi lokasi) {
        LokasiDTO lokasiDTO = new LokasiDTO();
        lokasiDTO.setNamaLokasi(lokasi.getNamaLokasi());
        lokasiDTO.setAlamat(lokasi.getAlamat());

        // Set ID Organisasi
        if (lokasi.getOrganisasi() != null) {
            lokasiDTO.setIdOrganisasi(lokasi.getOrganisasi().getId());
        }

        // Set Admin
        if (lokasi.getAdmin() != null) {
            AdminDTO adminDTO = convertAdminToDto(lokasi.getAdmin());
            lokasiDTO.setAdmin(adminDTO);

            // Set Admin ID
            lokasiDTO.setAdminId(lokasi.getAdmin().getId());
        }

        return lokasiDTO;
    }



}
