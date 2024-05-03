package com.example.absensireact.service;

import com.example.absensireact.model.Karyawan;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface KaryawanService {
    List<Karyawan> getAllKaryawan();



    Optional<Karyawan> getKaryawanByAdminId(Long adminId);

    Optional<Karyawan> getKaryawanById(Long id);




    Karyawan TambahKaryawan(Long adminId, Karyawan karyawan, MultipartFile image);

    String uploadFoto(MultipartFile image) throws IOException;


    Karyawan EditByid(Long id, Karyawan karyawan, MultipartFile image) throws IOException;

    void deleteKaryawan(Long id);
}
