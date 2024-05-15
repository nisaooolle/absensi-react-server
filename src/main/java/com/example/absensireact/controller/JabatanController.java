package com.example.absensireact.controller;

import com.example.absensireact.model.Jabatan;
import com.example.absensireact.service.JabatanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/jabatan/getbyid/{id}")
    public ResponseEntity<Jabatan> getJabatanById(@PathVariable Long id) {
        Optional<Jabatan> jabatan = jabatanService.getJabatanById(id);
        return jabatan.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/jabatan/getByAdmin/{adminId}")
    public ResponseEntity<List<Jabatan>> getJabatanByAdminId(@PathVariable Long adminId) {
        return ResponseEntity.ok(jabatanService.getJabatanByAdminId(adminId));
    }

    @PostMapping("/jabatan/add/{adminId}")
    public ResponseEntity<Jabatan> addJabatan(@PathVariable Long adminId, @RequestBody Jabatan jabatan) {
        Jabatan newJabatan = jabatanService.addJabatan(adminId, jabatan);
        return ResponseEntity.ok(newJabatan);
    }

    @PutMapping("/jabatan/edit/{adminId}")
    public ResponseEntity<Jabatan> editJabatan(@PathVariable Long adminId, @RequestBody Jabatan jabatan) {
        Jabatan updatedJabatan = jabatanService.editJabatan(adminId, jabatan);
        return ResponseEntity.ok(updatedJabatan);
    }
    @PutMapping("/jabatan/edit/{idd}")
    public ResponseEntity<Jabatan> editJabatanById(@PathVariable Long id, @RequestBody Jabatan jabatan) {
        Jabatan updatedJabatan = jabatanService.editJabatan(id, jabatan);
        return ResponseEntity.ok(updatedJabatan);
    }

    @DeleteMapping("/jabatan/delete/{idJabatan}")
    public ResponseEntity<String> deleteJabatan(@PathVariable Long idJabatan) {
        jabatanService.deleteJabatan(idJabatan);
        return ResponseEntity.ok("Jabatan deleted successfully.");
    }
}
