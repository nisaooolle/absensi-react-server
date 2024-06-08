package com.example.absensireact.controller;


import com.example.absensireact.config.AppConfig;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.OrganisasiRepository;
import com.example.absensireact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userImpl;

    @Autowired
    OrganisasiRepository organisasiRepository;

    @Autowired
    private AdminRepository adminRepository;



    @Autowired
    private AppConfig appConfig;



    @PostMapping("/user/register")
    public ResponseEntity<User> registerUser(@RequestBody User user, @RequestParam Long idOrganisasi) {
        Organisasi organisasi = organisasiRepository.findById(idOrganisasi)
                .orElseThrow(() -> new NotFoundException("Organisasi tidak ditemukan"));

        User newUser = userImpl.Register(user, idOrganisasi);

        return ResponseEntity.ok(newUser);
    }
    @GetMapping("/user/{idAdmin}/users")
    public List<User> getAllKaryawanByIdAdmin(@PathVariable Long idAdmin) {
        return userImpl.GetAllKaryawanByIdAdmin(idAdmin);
    }

    @PostMapping("/user/tambahkaryawan/{idAdmin}")
    public ResponseEntity<User> tambahKaryawan(@RequestBody User user, @PathVariable Long idAdmin, @RequestParam Long idOrganisasi, @RequestParam Long idJabatan, @RequestParam Long idShift) {
        try {
            User savedUser = userImpl.Tambahkaryawan(user, idAdmin, idOrganisasi, idJabatan, idShift);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/user/get-allUser")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userImpl.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/byJabatan/{idJabatan}")
    public ResponseEntity<List<User>> getUsersByJabatan(@PathVariable Long idJabatan) {
        try {
            List<User> users = userImpl.getAllByJabatan(idJabatan);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/byShift/{idShift}")
    public ResponseEntity<List<User>> getUsersByShift(@PathVariable Long idShift) {
        try {
            List<User> users = userImpl.getAllByShift(idShift);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/user/getUserBy/{id}")
    public ResponseEntity<User> GetUserById (@PathVariable Long id){
        User user = userImpl.getById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/editBY/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody  User user ) {
        try {
            User updatedUser = userImpl.edit(id, user );
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/user/edit-kar/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") Long id,
                                         @RequestParam(required = false) Long idJabatan,
                                         @RequestParam(required = false) Long idShift,
                                         @RequestBody User updatedUser) {
        try {
            User editedUser = userImpl.editUsernameJabatanShift(id, idJabatan, idShift, updatedUser);
            return ResponseEntity.ok(editedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/user/editFotoBY/{id}")
    public ResponseEntity<User> editFotoUser(@PathVariable Long id, @RequestPart("image") MultipartFile image) {
        try {
            User updatedUser = userImpl.fotoUser(id,image );
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/user/delete-foto/{id}")
    public ResponseEntity<String> deleteFoto(@PathVariable Long id) {
        try {
            userImpl.delete(id);
            return ResponseEntity.ok("User berhasil dihapus");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User tidak ditemukan dengan id: " + id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal menghapus user");
        }
    }
    @DeleteMapping("/user/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userImpl.deleteUser(id);
            return ResponseEntity.ok("User berhasil dihapus");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User tidak ditemukan dengan id: " + id);
        }
    }

}
