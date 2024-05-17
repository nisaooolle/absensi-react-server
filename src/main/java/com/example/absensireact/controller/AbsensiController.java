package com.example.absensireact.controller;


import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.impl.UserImpl;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AbsensiRepository;
import com.example.absensireact.service.AbsensiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AbsensiController {


    private final AbsensiService absensiService;

    private final AbsensiRepository absensiRepository;



    @Autowired
    public AbsensiController(AbsensiService absensiService , AbsensiRepository absensiRepository) {
        this.absensiService = absensiService;

        this.absensiRepository = absensiRepository;
    }

    @GetMapping("/absensi/getByUserId/{userId}")
    public ResponseEntity<List<Absensi>> getAbsensiByUserId(@PathVariable Long userId) {
        List<Absensi> absensi = absensiService.getAbsensiByUserId(userId);
        if (absensi.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(absensi, HttpStatus.OK);
    }

    @GetMapping("/absensi/checkAbsensi/{userId}")
    public ResponseEntity<String> checkAbsensiToday(@PathVariable Long userId) {
        if (absensiService.checkUserAlreadyAbsenToday(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body("Pengguna sudah melakukan absensi hari ini.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Pengguna belum melakukan absensi hari ini.");
        }
    }
    @GetMapping("/absensi/getAll")
    public ResponseEntity<List<Absensi>> getAllAbsensi() {
        List<Absensi> allAbsensi = absensiService.getAllAbsensi();
        return new ResponseEntity<>(allAbsensi, HttpStatus.OK);
    }
    @GetMapping("/absensi/getizin/{userId}")
    public ResponseEntity<List<Absensi>> getAbsensiByStatusIzin(@PathVariable Long userId) {
        List<Absensi> absensiList = absensiService.getByStatusAbsen(userId, "Izin");
        return new ResponseEntity<>(absensiList, HttpStatus.OK);
    }
    @GetMapping("/absensi/getData/{id}")
    public ResponseEntity<Absensi> getAbsensiById(@PathVariable Long id) {
        Optional<Absensi> absensi = absensiService.getAbsensiById(id);
        return absensi.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/absensi/izin/{userId}")
    public Absensi izin(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        String keteranganIzin = body.get("keteranganIzin");
        return absensiService.izin(userId, keteranganIzin);
    }
    @PutMapping("/absensi/izin-tengah-hari/{userId}")
    public Absensi izinTengahHari(@PathVariable Long userId ,@RequestBody Map<String , String> body)  {
        String keteranganPulangAwal = body.get("keteranganPulangAwal");
        return absensiService.izinTengahHari(userId , keteranganPulangAwal );
    }


    @PostMapping("/absensi/masuk/{userId}")
    public ResponseEntity<?> postAbsensiMasuk(@PathVariable Long userId,
                                              @RequestPart("image") MultipartFile image
                                             ) {
        try {
            Absensi absensi = absensiService.PostAbsensi(userId, image );
            return ResponseEntity.ok().body(absensi);
        } catch (IOException | EntityNotFoundException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
     @PutMapping("/absensi/pulang/{userId}")
    public ResponseEntity<?> putAbsensiPulang(@PathVariable Long userId,

                                              @RequestPart("image") MultipartFile image) {
        try {
            Absensi absensi = absensiService.Pulang(userId,image);
            return ResponseEntity.ok().body(absensi);
        } catch (IOException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
