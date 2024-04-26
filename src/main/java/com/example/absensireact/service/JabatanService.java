package com.example.absensireact.service;

import com.example.absensireact.model.Jabatan;

import java.util.List;

public interface JabatanService {
    List<Jabatan> getAllJabatan();
    List<Jabatan> getJabatanByAdminUserId(Long userId); // Renamed method
    Jabatan tambahJabatan(Long userId, Jabatan jabatan);
    Jabatan editJabatanByAdminUserId(Long userId, Jabatan jabatan); // Renamed method

    Jabatan editJabatanByAdminId(Long adminId, Jabatan jabatan);

    void deleteJabatan(Long idJabatan);
}

