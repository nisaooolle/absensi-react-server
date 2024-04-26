package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Jabatan;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.JabatanRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.role.RoleEnum;
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
    public List<Jabatan> getJabatanByAdminUserId(Long userId) {
        return jabatanRepository.findByUserIdAndAdminRole(userId);
    }

        @Override
        public Jabatan tambahJabatan(Long userId, Jabatan jabatan) {
            User admin = userRepository.findByIdAndRole(userId, RoleEnum.ADMIN)
                    .orElseThrow(() -> new NotFoundException("Admin not found with id: " + userId + " or not an ADMIN"));
            jabatan.setAdmin(admin);
            return jabatanRepository.save(jabatan);
        }

        @Override
        public Jabatan editJabatanByAdminUserId(Long userId, Jabatan jabatan) {
            User admin = userRepository.findByIdAndRole(userId, RoleEnum.ADMIN)
                    .orElseThrow(() -> new NotFoundException("Admin not found with id: " + userId + " or not an ADMIN"));
            jabatan.setAdmin(admin);
            return jabatanRepository.save(jabatan);
        }

    @Override
    public Jabatan editJabatanByAdminId(Long adminId, Jabatan jabatan) {
        return null;
    }

    @Override
    public void deleteJabatan(Long idJabatan) {
        if (!jabatanRepository.existsById(idJabatan)) {
            throw new NotFoundException("Jabatan not found with id: " + idJabatan);
        }
        jabatanRepository.deleteById(idJabatan);
    }
}
