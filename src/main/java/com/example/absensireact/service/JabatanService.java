package com.example.absensireact.service;

import com.example.absensireact.model.Jabatan;
import java.util.List;

public interface JabatanService {
    List<Jabatan> getAllJabatan();
    List<Jabatan> getJabatanByAdminId(Long adminId);
    Jabatan addJabatan(Long adminId, Jabatan jabatan);
    Jabatan editJabatan(Long adminId, Jabatan jabatan);
    void deleteJabatan(Long idJabatan);
}
