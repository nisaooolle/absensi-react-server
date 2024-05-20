package com.example.absensireact.controller;

import com.example.absensireact.dto.AdminDTO;
import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.dto.OrganisasiDTO;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Lokasi;
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

    @PostMapping("/lokasi/add/{adminId}")
    public ResponseEntity<LokasiDTO> createLokasi(@RequestBody LokasiDTO lokasiDTO) {
        return ResponseEntity.ok(lokasiService.saveLokasi(lokasiDTO));
    }
    @PostMapping("/tambah/{idAdmin}")
    public ResponseEntity<Lokasi> tambahLokasi(@PathVariable("idAdmin") Long idAdmin, @RequestBody Lokasi lokasi , @RequestParam Long idOrganisasi) {
        Lokasi lokasiBaru = lokasiService.tambahLokasi(idAdmin, lokasi , idOrganisasi);
        return ResponseEntity.ok(lokasiBaru);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<LokasiDTO>> getAllLokasi() {
        return ResponseEntity.ok(lokasiService.getAllLokasi());
    }

    @GetMapping("/GetById/{id}")
    public ResponseEntity<LokasiDTO> getLokasiById(@PathVariable Long id) {
        LokasiDTO lokasiDTO = lokasiService.getLokasiById(id);
        return ResponseEntity.ok(lokasiDTO);
    }
    @GetMapping("/get-admin/{idAdmin}")
    public ResponseEntity<List<Lokasi>>getLokasiByAdmin (@PathVariable Long idAdmin){
        List<Lokasi> lokasi = lokasiService.getAllByAdmin(idAdmin);
        return ResponseEntity.ok(lokasi);

    }

    @GetMapping("/GetOrganisasiById/{id}")
    public ResponseEntity<OrganisasiDTO> getOrganisasiById(@PathVariable Long id) {
        OrganisasiDTO organisasiDTO = lokasiService.getOrganisasiById(id);
        if (organisasiDTO != null) {
            return ResponseEntity.ok(organisasiDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/GetAdminById/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Long id) {
        AdminDTO adminDTO = lokasiService.getAdminById(id);
        if (adminDTO != null) {
            return ResponseEntity.ok(adminDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/Update/{id}")
    public ResponseEntity<LokasiDTO> updateLokasi(@PathVariable Long id, @RequestBody LokasiDTO lokasiDTO) {
        LokasiDTO updatedLokasi = lokasiService.updateLokasi(id, lokasiDTO);
        return ResponseEntity.ok(updatedLokasi);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteLokasi(@PathVariable Long id) {
        boolean deleted = lokasiService.deleteLokasi(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
