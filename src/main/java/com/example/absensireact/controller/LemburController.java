package com.example.absensireact.controller;

import com.example.absensireact.model.Lembur;
import com.example.absensireact.service.LemburService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class LemburController {

    @Autowired
    private LemburService lemburService;

    @GetMapping("/lembur/getall")
    public List<Lembur> getAllLembur() {
        return lemburService.getAllLembur();
    }

    @GetMapping("/lembur/getByid/{id}")
    public Lembur getLemburById(@PathVariable Long id) {
        return lemburService.getLemburById(id);
    }

    @GetMapping("/lembur/getByuserId/{id}")
    public Lembur getLemburByUserId(@PathVariable Long userId) {
        return lemburService.getLemburByUserId(userId);
    }

    @PostMapping("/lembur/tambahLembur/{userId}")
    public Lembur createLembur(@PathVariable Long userId ,@RequestBody Lembur lembur) {
        return lemburService.IzinLembur(userId , lembur);
    }

    @PutMapping("/lembur/ubahLembur/{id}")
    public Lembur updateLembur(@PathVariable Long id, @RequestBody Lembur lembur) {
        return lemburService.updateLembur(id, lembur);
    }

    @DeleteMapping("/lembur/delete/{id}")
    public void deleteLembur(@PathVariable Long id) {
        lemburService.deleteLembur(id);
    }
}

