package com.example.absensireact.controller;

import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.service.LokasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lokasi")
public class LokasiController {

    @Autowired
    private LokasiService lokasiService;

    @PostMapping
    public ResponseEntity<LokasiDTO> createLokasi(@RequestBody LokasiDTO lokasiDTO) {
        return ResponseEntity.ok(lokasiService.saveLokasi(lokasiDTO));
    }

    @GetMapping
    public ResponseEntity<List<LokasiDTO>> getAllLokasi() {
        return ResponseEntity.ok(lokasiService.getAllLokasi());
    }

    @GetMapping("/GetById/{id}")
    public ResponseEntity<LokasiDTO> getLokasiById(@PathVariable Integer id) {
        LokasiDTO lokasiDTO = lokasiService.getLokasiById(id);
        if (lokasiDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lokasiDTO);
    }

    @GetMapping("/{id}/organisasi")
    public ResponseEntity<Organisasi> getOrganisasiByLokasiId(@PathVariable Integer id) {
        Organisasi organisasi = lokasiService.getOrganisasiByLokasiId(id);
        if (organisasi == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(organisasi);
    }

    @GetMapping("/{id}/admin")
    public ResponseEntity<Admin> getAdminByLokasiId(@PathVariable Integer id) {
        Admin admin = lokasiService.getAdminByLokasiId(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<LokasiDTO> updateLokasi(@PathVariable Integer id, @RequestBody LokasiDTO lokasiDTO) {
        LokasiDTO updatedLokasi = lokasiService.updateLokasi(id, lokasiDTO);
        if (updatedLokasi == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedLokasi);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteLokasi(@PathVariable Integer id) {
        boolean isDeleted = lokasiService.deleteLokasi(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
