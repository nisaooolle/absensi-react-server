package com.example.absensireact.service;

import com.example.absensireact.model.Karyawan;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface KaryawanService {
    List<Karyawan> getAllKaryawan();


    List<Karyawan> getKaryawanByUserId(Long userId);

    Optional<Karyawan> getKaryawanById(Long id);


    //    @Override
//    public Karyawan TambahKaryawan(Long userId , Karyawan karyawan){
//        // Dapatkan objek User dari UserRepository berdasarkan ID
//        User user = userRepository.findById(userId).orElse(null);
//
//        if (user != null) {
//            karyawan.setUser(user);
//            karyawan.setJabatan(karyawan.getJabatan());
//            karyawan.setShift(karyawan.getShift());
//            karyawan.setUser(user);
//            return karyawanRepository.save(karyawan);
//        } else {
//            throw new EntityNotFoundException("User dengan ID " + karyawan.getUser().getId() + " tidak ditemukan.");
//        }
//    }
    Karyawan TambahKaryawan(Long userId, Karyawan karyawan);

    String uploadFoto(MultipartFile image) throws IOException;

    Karyawan EditKaryawanByUserId(Long userId, Karyawan karyawan, MultipartFile image) throws IOException;

    Karyawan EditByid(Long id, Karyawan karyawan, MultipartFile image) throws IOException;

    void deleteKaryawan(Long id);
}
