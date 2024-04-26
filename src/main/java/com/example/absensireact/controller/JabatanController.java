package com.example.absensireact.controller;

import com.example.absensireact.model.Jabatan;
import com.example.absensireact.service.JabatanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JabatanController {

    private final JabatanService jabatanService;

    public JabatanController(JabatanService jabatanService) {
        this.jabatanService = jabatanService;
    }

    @GetMapping("/jabatan/all")
    public ResponseEntity<List<Jabatan>> getAllJabatan() {
        return ResponseEntity.ok(jabatanService.getAllJabatan());
    }

    @GetMapping("/jabatan/getByAdmin/{userId}")
    public ResponseEntity<List<Jabatan>> getJabatanByAdminUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(jabatanService.getJabatanByAdminUserId(userId));
    }

    @PostMapping("/jabatan/tambah/{userId}")
    public ResponseEntity<Jabatan> addJabatan(@PathVariable Long userId, @RequestBody Jabatan jabatan) {
        Jabatan newJabatan = jabatanService.tambahJabatan(userId, jabatan);
        return ResponseEntity.ok(newJabatan);
    }

    @PutMapping("/jabatan/editByAdmin/{userId}")
    public ResponseEntity<Jabatan> editJabatanByAdminUserId(@PathVariable Long userId, @RequestBody Jabatan jabatan) {
        Jabatan updatedJabatan = jabatanService.editJabatanByAdminUserId(userId, jabatan);
        return ResponseEntity.ok(updatedJabatan);
    }

    @DeleteMapping("/jabatan/delete/{idJabatan}")
    public ResponseEntity<String> deleteJabatan(@PathVariable Long idJabatan) {
        jabatanService.deleteJabatan(idJabatan);
        return ResponseEntity.ok("Jabatan deleted successfully.");
    }
}
