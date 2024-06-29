package com.example.absensireact.controller;

import com.example.absensireact.helper.LemburPDF;
import com.example.absensireact.model.Lembur;
import com.example.absensireact.service.LemburService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class LemburController {

    @Autowired
    private LemburService lemburService;

    @Autowired
    private LemburPDF lemburPDF;

    @GetMapping("/lembur/download-pdf/{id}")
    public void downloadPDF(@PathVariable Long id, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        lemburPDF.generatePDF(id, baos);

        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=lembur.pdf");
        response.setContentLength(baos.size());

        // Write PDF content to response output stream
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }
    @GetMapping("/lembur/getall")
    public List<Lembur> getAllLembur() {
        return lemburService.getAllLembur();
    }

    @GetMapping("/lembur/getByid/{id}")
    public Lembur getLemburById(@PathVariable Long id) {
        return lemburService.getLemburById(id);
    }

    @GetMapping("/lembur/getByuserId/{userId}")
    public List<Lembur> getLemburByUserId(@PathVariable Long userId) {
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

