package com.example.absensireact.service;

import com.example.absensireact.model.Jabatan;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JabatanService {

    List<Jabatan> getAllJabatan();
    List<Jabatan> getJabatanByAdminId(Long adminId);
    Optional<Jabatan> getJabatanById(Long id);
    Jabatan addJabatan(Long adminId, Jabatan jabatan);
    Jabatan editJabatan(Long adminId, Jabatan jabatan); // Corrected method signature

    Jabatan editJabatanById(Long id, Jabatan jabatan);

    Map<String, Boolean> deleteJabatan(Long idJabatan);
}
