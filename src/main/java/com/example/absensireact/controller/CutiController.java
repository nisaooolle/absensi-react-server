package com.example.absensireact.controller;


import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.helper.CutiPDF;
import com.example.absensireact.model.Cuti;
import com.example.absensireact.service.CutiService;
import com.example.absensireact.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CutiController {
    private final CutiService cutiService;

    private final UserService userService;

    private final CutiPDF cutiPDF;

    public CutiController(CutiService cutiService, UserService userService, CutiPDF cutiPDF) {
        this.cutiService = cutiService;
        this.userService = userService;
        this.cutiPDF = cutiPDF;
    }

    @GetMapping(value = "/cuti/{id}/download")
    public void downloadCutiPDF(@PathVariable Long id, HttpServletResponse response) {
        Optional<Cuti> cutiOptional = cutiService.GetCutiById(id);
        if (cutiOptional.isPresent()) {
            Cuti cuti = cutiOptional.get();
            try {
                cutiPDF.downloadPDF(cuti, response);
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @GetMapping("/cuti/getById/{id}")
    public Optional<Cuti> GetCutiById(@PathVariable Long id ){
        Optional<Cuti> cutiOptional = cutiService.GetCutiById(id);
        if (cutiOptional.isEmpty()) {
            throw new NotFoundException("Cuti tidak ditemukan dengan id : " + id);
        }
        return cutiOptional;
    }

    @GetMapping("/cuti/getByUser/{userId}")
    public Cuti GetCutiByUserId(@PathVariable Long userId){
        Cuti cuti = (Cuti) cutiService.GetCutiByUserId(userId);
        if (cuti == null) {
            throw new NotFoundException("User id tidak ditemukan dengan id : " + userId);
        }
        return cuti;

    }

    @GetMapping("/cuti/getall")
    public ResponseEntity<List<Cuti>> getAllCuti() {
        List<Cuti> cutiList = cutiService.GetCutiAll();
        return new ResponseEntity<>(cutiList, HttpStatus.OK);
    }

    @PostMapping("/cuti/tambahCuti/{userId}")
    public ResponseEntity<Cuti> createCuti(@PathVariable Long userId , @RequestBody Cuti cuti) {
        Cuti createdCuti = cutiService.IzinCuti( userId , cuti);
        return new ResponseEntity<>(createdCuti, HttpStatus.CREATED);
    }

    @PutMapping("/cuti/tolak-cuti/{id}")
    public ResponseEntity<Cuti> tolakCuti(@PathVariable Long id, @RequestBody Cuti cuti) {
        Cuti updatedCuti = cutiService.TolakCuti(id, cuti);
        if (updatedCuti == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCuti, HttpStatus.OK);
    }
    @PutMapping("/cuti/terima-cuti/{id}")
    public ResponseEntity<Cuti> terimaCuti(@PathVariable Long id, @RequestBody Cuti cuti) {
        Cuti updatedCuti = cutiService.TerimaCuti(id, cuti);
        if (updatedCuti == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCuti, HttpStatus.OK);
    }
    @PutMapping("/cuti/update-cuti-user/{id}")
    public ResponseEntity<Cuti> updateCutiById(@PathVariable Long id, @RequestBody Cuti updatedCuti) {
        Cuti updated = cutiService.updateCutiById(id, updatedCuti);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/cuti/delete/{id}")
    public ResponseEntity<Void> deleteCuti(@PathVariable Long id) {
        boolean deleted = cutiService.deleteCuti(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
