package com.example.absensireact.controller;

import com.example.absensireact.model.Karyawan;
import com.example.absensireact.service.KaryawanService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/karyawan/getByUser/{adminId}")
    public ResponseEntity<Optional<Karyawan>> getKaryawanByUserId(@PathVariable Long adminId) {
        Optional<Karyawan> karyawanList = karyawanService.getKaryawanByAdminId(adminId);
        return ResponseEntity.ok(karyawanList);
    }

    @GetMapping("/karyawan/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Karyawan> karyawan = karyawanService.getKaryawanById(id);

        if (karyawan.isPresent()) {
            return new ResponseEntity<>(karyawan.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Karyawan tidak ditemukan", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/karyawan/tambah/{adminId}")
    public ResponseEntity<Karyawan> addKaryawan(@PathVariable Long adminId ,@RequestBody Karyawan karyawan) {
        Karyawan newKaryawan = karyawanService.TambahKaryawan(adminId , karyawan);
        return ResponseEntity.ok(newKaryawan);
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