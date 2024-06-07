package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Jabatan;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.JabatanRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.JabatanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JabatanImpl implements JabatanService {


    @Autowired
    private UserRepository userRepository;
    private final JabatanRepository jabatanRepository;
    private final AdminRepository adminRepository;


    public JabatanImpl(AdminRepository adminRepository, JabatanRepository jabatanRepository ) {
        this.adminRepository = adminRepository;
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
    public Optional<Jabatan> getJabatanById(Long idJabatan) {
        return  jabatanRepository.findById(idJabatan);
    }

    @Override
    public Jabatan addJabatan(Long adminId, Jabatan jabatan ) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException(  "Admin not found with id: " + adminId));

        jabatan.setAdmin(admin);
        jabatan.setNamaJabatan(jabatan.getNamaJabatan());
        return jabatanRepository.save(jabatan);
    }

//    @Override
//    public Jabatan TambahJabatan (Long adminId , Jabatan jabatan){
//        Admin admin = adminRepository.findById(adminId)
//                .orElseThrow(() -> new NotFoundException(  "Admin not found with id: " + adminId));
//
//        List<User> userList = userRepository.findByIdJabatan(id);
//        String jmlhKaryawan = String.valueOf(userList.size());
//
//        jabatan.setJumlahKaryawan(jmlhKaryawan);
//        jabatan.setAdmin(admin);
//        jabatan.setNamaJabatan(jabatan.getNamaJabatan());
//        return jabatanRepository.save(jabatan);
//    }
    @Override
    public Jabatan editJabatan(Long adminId, Jabatan jabatan) {
        if (jabatan.getIdJabatan() == null || jabatan.getIdJabatan() == 0) {
            throw new IllegalArgumentException("Jabatan ID must be provided for editing.");
        }

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin not found with id: " + adminId));

        Jabatan existingJabatan = jabatanRepository.findById(jabatan.getIdJabatan())
                .orElseThrow(() -> new NotFoundException("Jabatan not found with id: " + jabatan.getIdJabatan()));

        // Check if the provided admin is the same as the admin of the existing jabatan
        if (!existingJabatan.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("You are not authorized to edit this Jabatan.");
        }

        jabatan.setAdmin(admin);
        return jabatanRepository.save(jabatan);
    }

    @Override
    public Jabatan editJabatanById(Long id, Jabatan jabatan) {

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found with id: " + id));

        Jabatan existingJabatan = jabatanRepository.findById(jabatan.getIdJabatan())
                .orElseThrow(() -> new NotFoundException("Jabatan not found with id: " + jabatan.getIdJabatan()));


        if (!existingJabatan.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("You are not authorized to edit this Jabatan.");
        }


        jabatan.setNamaJabatan(jabatan.getNamaJabatan());
        jabatan.setJumlahKaryawan(jabatan.getJumlahKaryawan());
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
