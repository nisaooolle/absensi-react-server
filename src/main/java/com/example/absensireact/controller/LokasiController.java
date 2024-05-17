package com.example.absensireact.controller;

import com.example.absensireact.dto.LokasiDTO;
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
        return ResponseEntity.ok(lokasiDTO);
    }
    @PutMapping("/Update/{id}")
    public ResponseEntity<LokasiDTO> updateLokasi(@PathVariable Integer id, @RequestBody LokasiDTO lokasiDTO) {
        LokasiDTO updatedLokasi = lokasiService.updateLokasi(id, lokasiDTO);
        return ResponseEntity.ok(updatedLokasi);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteLokasi(@PathVariable Integer id) {
        boolean deleted = lokasiService.deleteLokasi(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
