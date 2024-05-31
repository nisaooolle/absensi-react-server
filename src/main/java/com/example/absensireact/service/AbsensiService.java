package com.example.absensireact.service;

import com.example.absensireact.model.Absensi;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface  AbsensiService {


    List<Absensi> getAllAbsensi();


    List<Absensi> getAbsensiByTanggal(Date tanggalAbsen);

    Absensi PostAbsensi(Long userId, MultipartFile image, String lokasiMasuk , String keteranganTerlambat) throws IOException;



    Absensi Pulang(Long userId, MultipartFile image, String lokasiPulang, String keteranganPulangAwal) throws IOException;

    boolean checkUserAlreadyAbsenToday(Long userId);

    Absensi izin(Long userId, String keteranganIzin)  ;

    Absensi izinTengahHari(Long userId, String keteranganPulangAwal)  ;

    List<Absensi>getByStatusAbsen(Long userId, String statusAbsen);

    Optional<Absensi> getAbsensiById(Long id);

    Absensi updateAbsensi(Long id, Absensi absensi);


    void deleteAbsensi(Long id) throws IOException;

    List<Absensi> getAbsensiByUserId(Long userId);







}
