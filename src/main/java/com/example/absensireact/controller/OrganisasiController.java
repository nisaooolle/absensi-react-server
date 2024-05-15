package com.example.absensireact.controller;

import com.example.absensireact.model.Organisasi;
import com.example.absensireact.service.OrganisasiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class OrganisasiController {


    private final OrganisasiService organisasiService;

    public OrganisasiController(OrganisasiService organisasiService) {
        this.organisasiService = organisasiService;
    }

    @GetMapping("/organisasi/all")
    public ResponseEntity<List<Organisasi>> getAllOrganisasi() {
        List<Organisasi> organisasiList =  organisasiService.getAllOrganisasi();
        return ResponseEntity.ok(organisasiList);
    }
    @GetMapping("/organisasi/getAllByAdmin/{idAdmin}")
    public ResponseEntity<Organisasi> getAllByadmin(@PathVariable Long idAdmin){
        Optional<Organisasi> organisasiList = organisasiService.GetAllByIdAdmin(idAdmin);
        return organisasiList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/organisasi/getById/{id}")
    public ResponseEntity<Organisasi> getOrganisasiById(@PathVariable Long id) {
        Optional<Organisasi> organisasi = organisasiService.GetOrganisasiById(id);
        return organisasi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/organisasi/tambahByIdAdmin/{idAdmin}")
    public ResponseEntity<Organisasi> tambahOrganisasi(
            @PathVariable Long idAdmin,
            @RequestPart("organisasi") String organisasiJson,
            @RequestPart("image") MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Organisasi organisasi = objectMapper.readValue(organisasiJson, Organisasi.class);

        Organisasi savedOrganisasi = organisasiService.TambahOrganisasi(idAdmin, organisasi, image);
        return ResponseEntity.ok(savedOrganisasi);
    }


    @PutMapping("/organisasi/putByIdAdmin/{idAdmin}" )
    public ResponseEntity<Organisasi> ubahDataOrganisasi(
            @PathVariable Long idAdmin,
            @RequestBody Organisasi organisasi,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        Organisasi updatedOrganisasi = organisasiService.UbahDataOrgannisasi(idAdmin, organisasi, image);
        return ResponseEntity.ok(updatedOrganisasi);
    }

    @PutMapping("/organisasi/editById/{id}" )
    public ResponseEntity<Organisasi> editOrganisasi(
            @PathVariable Long id,
            @RequestBody Organisasi organisasi,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        Organisasi updatedOrganisasi = organisasiService.EditByid(id, organisasi, image);
        return ResponseEntity.ok(updatedOrganisasi);
    }

    @DeleteMapping("/organisasi/delete/{id}")
    public ResponseEntity<Void> deleteOrganisasi(@PathVariable Long id) {
        organisasiService.deleteKaryawan(id);
        return ResponseEntity.noContent().build();
    }
}