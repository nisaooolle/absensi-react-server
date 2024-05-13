package com.example.absensireact.service;

import com.example.absensireact.model.Absensi;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface  AbsensiService {


    List<Absensi> getAllAbsensi();


    Absensi Pulang(Long userId, MultipartFile image) throws IOException;

    boolean checkUserAlreadyAbsenToday(Long userId);

    Optional<Absensi> getAbsensiById(Long id);

    Absensi updateAbsensi(Long id, Absensi absensi);


    void deleteAbsensi(Long id);

    List<Absensi> getAbsensiByUserId(Long userId);



    Absensi PostAbsensi(Long userId, MultipartFile image , String keteranganTerlambat) throws IOException;




}
