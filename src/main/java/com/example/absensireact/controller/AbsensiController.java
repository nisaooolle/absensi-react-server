package com.example.absensireact.controller;


import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.impl.UserImpl;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.service.AbsensiService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AbsensiController {


    private final AbsensiService absensiService;



    @Autowired
    public AbsensiController(AbsensiService absensiService , UserImpl userImpl) {
        this.absensiService = absensiService;

    }

    @GetMapping("/absensi/getByUserId/{userId}")
    public ResponseEntity<List<Absensi>> getAbsensiByUserId(@PathVariable Long userId) {
        List<Absensi> absensi = absensiService.getAbsensiByUserId(userId);
        if (absensi.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(absensi, HttpStatus.OK);
    }

    @GetMapping("/absensi/getAll")
    public ResponseEntity<List<Absensi>> getAllAbsensi() {
        List<Absensi> allAbsensi = absensiService.getAllAbsensi();
        return new ResponseEntity<>(allAbsensi, HttpStatus.OK);
    }

    @GetMapping("/absensi/getData/{id}")
    public ResponseEntity<Absensi> getAbsensiById(@PathVariable Long id) {
        Optional<Absensi> absensi = absensiService.getAbsensiById(id);
        return absensi.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping("/absensi/absensi-masuk")
    public ResponseEntity<Absensi> postAbsensi(@RequestParam("image") MultipartFile image,
                                               @RequestParam("userId") Long userId) throws IOException {
        Absensi newAbsensi = absensiService.PostAbsensi(userId, image);
        return new ResponseEntity<>(newAbsensi, HttpStatus.CREATED);
    }

    @PutMapping("/absensi/absensi-pulang/{id}")
    public ResponseEntity<Absensi> PutPulang(@PathVariable Long id,
                                             @RequestParam("image") MultipartFile image) {
        try {
            Absensi updatedAbsensi = absensiService.PutPulang(id, image);
            return ResponseEntity.ok(updatedAbsensi);
        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/absensi/update/{id}")
    public ResponseEntity<Absensi> updateAbsensi(@PathVariable Long id, @RequestBody Absensi absensi) {
        Absensi updatedAbsensi = absensiService.updateAbsensi(id, absensi);
        return new ResponseEntity<>(updatedAbsensi, HttpStatus.OK);
    }

    @DeleteMapping("/absensi/delete/{id}")
    public ResponseEntity<?> deleteAbsensi(@PathVariable Long id) {
        absensiService.deleteAbsensi(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
