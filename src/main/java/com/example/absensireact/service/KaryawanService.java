package com.example.absensireact.service;

import com.example.absensireact.model.Karyawan;

import java.util.List;

public interface KaryawanService {
    List<Karyawan> getAllKaryawan();

    List<Karyawan> getKaryawanByUserId(Long userId);

    Karyawan TambahKaryawan(Long userId, Karyawan karyawan);

    Karyawan EditKaryawanByUserId(Long userId, Karyawan karyawan);

    void deleteKaryawan(Long id);
}
