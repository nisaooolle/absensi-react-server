package com.example.absensireact.controller;

import com.example.absensireact.model.Karyawan;
import com.example.absensireact.service.KaryawanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class KaryawanController {

    private final KaryawanService karyawanService;

    public KaryawanController(KaryawanService karyawanService) {
        this.karyawanService = karyawanService;
    }

    @GetMapping("/karyawan/all")
    public ResponseEntity<List<Karyawan>> getAllKaryawan() {
        List<Karyawan> karyawanList = karyawanService.getAllKaryawan();
        return ResponseEntity.ok(karyawanList);
    }

    @GetMapping("/karyawan/getByUser/{userId}")
    public ResponseEntity<List<Karyawan>> getKaryawanByUserId(@PathVariable Long userId) {
        List<Karyawan> karyawanList = karyawanService.getKaryawanByUserId(userId);
        return ResponseEntity.ok(karyawanList);
    }

    @GetMapping("/karyawan/getById/{id}")
    public ResponseEntity<Optional<Karyawan>>getById(@PathVariable Long id){
        Optional<Karyawan> karyawan = karyawanService.getKaryawanById(id);
        if (karyawan.isPresent()) {
            return ResponseEntity.ok(karyawan);
        }
        throw new RuntimeException("Gagal mengubah data Karyawan dengan id" + id);
    }

    @PostMapping("/karyawan/tambah/{userId}")
    public ResponseEntity<Karyawan> addKaryawan(@PathVariable Long userId ,@RequestBody Karyawan karyawan) {
        Karyawan newKaryawan = karyawanService.TambahKaryawan(userId , karyawan);
        return ResponseEntity.ok(newKaryawan);
    }

    @PutMapping("/karyawan/editByuser/{userId}")
    public ResponseEntity<Karyawan> editKaryawanByUserId(@PathVariable Long userId, @RequestBody Karyawan karyawan , @RequestParam("image") MultipartFile image) throws IOException {
        Karyawan updatedKaryawan = karyawanService.EditKaryawanByUserId(userId, karyawan , image);
        return ResponseEntity.ok(updatedKaryawan);
    }
    @PutMapping("/karyawan/editById/{id}")
    public ResponseEntity<Karyawan> editById(@PathVariable Long id, @RequestBody Karyawan karyawan , @RequestParam("image") MultipartFile image) throws IOException {
        Karyawan updatedKaryawan = karyawanService.EditByid(id, karyawan , image);
        return ResponseEntity.ok(updatedKaryawan);
    }

    @DeleteMapping("/karyawan/delete/{id}")
    public ResponseEntity<?> deleteKaryawan(@PathVariable Long id) {
        karyawanService.deleteKaryawan(id);
        return ResponseEntity.noContent().build();
    }
}