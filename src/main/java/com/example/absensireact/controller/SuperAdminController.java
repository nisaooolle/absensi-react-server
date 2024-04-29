package com.example.absensireact.controller;

import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import com.example.absensireact.service.SuperAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class SuperAdminController {
    private final SuperAdminService superAdminService;


    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }


    @GetMapping("/superadmin/getAll")
    public ResponseEntity<List<SuperAdmin>> getAllSuperAdmin() {
        List<SuperAdmin> superAdmins = (List<SuperAdmin>) superAdminService.getAllSuperAdmin();
        return ResponseEntity.ok(superAdmins);
    }

    @GetMapping("/superadmin/getbyid/{id}")
    public ResponseEntity<SuperAdmin> getSuperAdminById(@PathVariable Long id) {
        Optional<SuperAdmin> superAdmin = superAdminService.getSuperadminbyId(id);
        return superAdmin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/superadmin/register")
    public ResponseEntity<SuperAdmin> register(@RequestBody SuperAdmin superAdmin){

        return ResponseEntity.ok( superAdminService.RegisterSuperAdmin(superAdmin));
    }


    @PostMapping("/superadmin/tambahdata/{id} ")
    public ResponseEntity<SuperAdmin> tambahSuperAdmin(@PathVariable Long id,
                                                       @RequestBody SuperAdmin superAdmin,
                                                       @RequestParam("image") MultipartFile image) {
        SuperAdmin newSuperAdmin = superAdminService.tambahSuperAdmin(id, superAdmin, image);
        return ResponseEntity.ok(newSuperAdmin);
    }

    @PutMapping("/superadmin/edit/{id}")
    public ResponseEntity<SuperAdmin> editSuperAdmin(@PathVariable Long id,
                                                     @RequestParam("image") MultipartFile image,
                                                     @RequestBody SuperAdmin superAdmin) {
        SuperAdmin editedSuperAdmin = superAdminService.EditSuperAdmin(id, image, superAdmin);
        return ResponseEntity.ok(editedSuperAdmin);
    }

    @DeleteMapping("/superadmin/delete/{id}")
    public ResponseEntity<Void> deleteSuperAdmin(@PathVariable Long id) {
        superAdminService.deleteSuperAdmin(id);
        return ResponseEntity.ok().build();
    }
}
