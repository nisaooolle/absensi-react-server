package com.example.absensireact.controller;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Izin;
import com.example.absensireact.service.IzinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class IzinController {

    @Autowired
    private IzinService izinService;

    @GetMapping("/izin/getAll")
    public List<Izin> getAllIzin() {
        return izinService.getAllIzin();
    }

    @GetMapping("/izin/getByUserId/{userId}")
    public Optional<Izin> getIzinByUserId(@PathVariable Long userId) {
        Optional<Izin> izin = izinService.getIzinByUserId(userId);
        if (izin.isEmpty()) {
            throw new NotFoundException("User dengan ID " + userId + " tidak ditemukan");
        }
        return izin;
    }
    @GetMapping("/izin/getbyId/{id}")
    public ResponseEntity<Izin> getIzinById(@PathVariable Long id) {
        Izin izin = izinService.getIzinById(id);
        if (izin != null) {
            return new ResponseEntity<>(izin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/izin/tambahIzin/{userId}")
    public ResponseEntity<Izin> createIzin(@PathVariable Long userId, @RequestBody Izin izin) {
        try {
            Izin createdIzin = izinService.createIzin(userId, izin);
            return new ResponseEntity<>(createdIzin, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/izin/editBy/{id}")
    public Izin editIzinByid(@PathVariable Long id, @RequestBody Izin izin) {
        Izin izinCek = izinService.getIzinById(id);
        if (izinCek == null) {
            throw new NotFoundException("Izin tidak ditemukan");
        }
        izinCek.setKeternganIzin(izin.getKeternganIzin());
        return izinService.editIzinByid(id, izinCek);
    }

    @DeleteMapping("/izin/delete/{id}")
    public ResponseEntity<HttpStatus> deleteIzin(@PathVariable Long id) {
        try {
            izinService.deleteIzin(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
