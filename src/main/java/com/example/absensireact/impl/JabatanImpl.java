package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Jabatan;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.JabatanRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.JabatanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JabatanImpl implements JabatanService {

    private final UserRepository userRepository;
    private final JabatanRepository jabatanRepository;

    public JabatanImpl(UserRepository userRepository, JabatanRepository jabatanRepository) {
        this.userRepository = userRepository;
        this.jabatanRepository = jabatanRepository;
    }

    @Override
    public List<Jabatan> getAllJabatan() {
        return jabatanRepository.findAll();
    }

    @Override
    public List<Jabatan> getJabatanByAdminId(Long adminId) {
        return jabatanRepository.findByAdminId(adminId);
    }

    @Override
    public Jabatan tambahJabatan(Long adminId, Jabatan jabatan) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin not found with id: " + adminId));
        jabatan.setAdmin(admin);
        return jabatanRepository.save(jabatan);
    }

    @Override
    public Jabatan editJabatanByAdminId(Long adminId, Jabatan jabatan) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new NotFoundException("Admin not found with id: " + adminId));
        jabatan.setAdmin(admin);
        return jabatanRepository.save(jabatan);
    }

    @Override
    public void deleteJabatan(Long idJabatan) {
        if (!jabatanRepository.existsById(idJabatan)) {
            throw new NotFoundException("Jabatan not found with id: " + idJabatan);
        }
        jabatanRepository.deleteById(idJabatan);
    }
}
