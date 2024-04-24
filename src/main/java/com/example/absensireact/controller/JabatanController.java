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

    @GetMapping("/jabatan/getByAdmin/{adminId}")
    public ResponseEntity<List<Jabatan>> getJabatanByAdminId(@PathVariable Long adminId) {
        return ResponseEntity.ok(jabatanService.getJabatanByAdminId(adminId));
    }

    @PostMapping("/jabatan/tambah/{adminId}")
    public ResponseEntity<Jabatan> addJabatan(@PathVariable Long adminId, @RequestBody Jabatan jabatan) {
        Jabatan newJabatan = jabatanService.tambahJabatan(adminId, jabatan);
        return ResponseEntity.ok(newJabatan);
    }

    @PutMapping("/jabatan/editByAdmin/{adminId}")
    public ResponseEntity<Jabatan> editJabatanByAdminId(@PathVariable Long adminId, @RequestBody Jabatan jabatan) {
        Jabatan updatedJabatan = jabatanService.editJabatanByAdminId(adminId, jabatan);
        return ResponseEntity.ok(updatedJabatan);
    }

    @DeleteMapping("/jabatan/delete/{idJabatan}")
    public ResponseEntity<String> deleteJabatan(@PathVariable Long idJabatan) {
        jabatanService.deleteJabatan(idJabatan);
        return ResponseEntity.ok("Jabatan deleted successfully.");
    }
}
