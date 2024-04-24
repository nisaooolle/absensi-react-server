package com.example.absensireact.controller;

import com.example.absensireact.model.Admin;
import com.example.absensireact.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin/register")
    public ResponseEntity<Admin> registerAdmin(@RequestBody Admin admin) {
        Admin registeredAdmin = adminService.RegisterAdmin(admin);
        return new ResponseEntity<>(registeredAdmin, HttpStatus.CREATED);
    }

    @GetMapping("/admin/getById/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAll();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        Admin updatedAdmin = adminService.edit(id, admin);
        return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAdmin(@PathVariable Long id) {
        Map<String, Boolean> response = adminService.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
