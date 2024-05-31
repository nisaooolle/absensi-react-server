package com.example.absensireact.controller;


import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.helper.AbsensiExportService;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.repository.AbsensiRepository;
import com.example.absensireact.service.AbsensiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AbsensiController {


    @Autowired
    private AbsensiExportService absensiExportService;
    private final AbsensiService absensiService;

    private final AbsensiRepository absensiRepository;



    @Autowired
    public AbsensiController(AbsensiService absensiService , AbsensiRepository absensiRepository) {
        this.absensiService = absensiService;

        this.absensiRepository = absensiRepository;
    }

    @GetMapping("/absensi/rekap-perkaryawan/export")
    public ResponseEntity<?> exportAbsensiToExcel() {
        try {
            ByteArrayInputStream byteArrayInputStream = absensiExportService.RekapPerkaryawan();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=absensi.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArrayInputStream.readAllBytes());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to export data");
        }
    }

    @GetMapping("/absensi/rekap/export/{userId}")
    public ResponseEntity<?> exportAbsensiByUserId(@PathVariable Long userId) {
        try {
            ByteArrayInputStream byteArrayInputStream = absensiExportService.RekapPerkaryawanByUserId(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=absensi.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArrayInputStream.readAllBytes());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to export data");
        }
    }

    @GetMapping("/getByTanggal/{tanggal}")
    public ResponseEntity<List<Absensi>> getAbsensiByTanggal(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tanggalAbsen) {
        List<Absensi> absensiList = absensiService.getAbsensiByTanggal(tanggalAbsen);
        return ResponseEntity.ok(absensiList);
    }

//    @GetMapping("/rekap/export/{tanggal}")
//    public ResponseEntity<?> exportAbsensiByTanggal(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tanggalAbsen) {
//        try {
//            ByteArrayInputStream byteArrayInputStream = absensiExportService.exportAbsensiByTanggal(tanggalAbsen);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment; filename=absensi.xlsx");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(byteArrayInputStream.readAllBytes());
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Failed to export data");
//        }
//    }
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
                                              @RequestPart("image") MultipartFile image ,
                                              @RequestParam("lokasiMasuk") String lokasiMasuk,
                                              @RequestParam("keteranganTerlambat") String keteranganTerlambat
                                             ) {
        try {
            Absensi absensi = absensiService.PostAbsensi(userId, image , lokasiMasuk , keteranganTerlambat);
            return ResponseEntity.ok().body(absensi);
        } catch (IOException | EntityNotFoundException | NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
     @PutMapping("/absensi/pulang/{userId}")
    public ResponseEntity<?> putAbsensiPulang(@PathVariable Long userId,
                                              @RequestPart("image") MultipartFile image,
                                              @RequestParam("lokasiPulang") String lokasiPulang,
                                              @RequestParam("keteranganPulangAwal") String keteranganPulangAwal
     ) {
        try {
            Absensi absensi = absensiService.Pulang(userId ,image , lokasiPulang , keteranganPulangAwal );
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
    public ResponseEntity<?> deleteAbsensi(@PathVariable Long id) throws IOException {
        absensiService.deleteAbsensi(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
