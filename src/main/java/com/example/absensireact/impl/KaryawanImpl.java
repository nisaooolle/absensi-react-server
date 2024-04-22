package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Karyawan;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.KaryawanRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.KaryawanService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class KaryawanImpl implements KaryawanService {
      private final UserRepository userRepository;

      private final KaryawanRepository karyawanRepository;

    public KaryawanImpl(UserRepository userRepository, KaryawanRepository karyawanRepository) {
        this.userRepository = userRepository;
        this.karyawanRepository = karyawanRepository;
    }

    @Override
    public List<Karyawan>getAllKaryawan(){
        return karyawanRepository.findAll();
    }

    @Override
    public List<Karyawan> getKaryawanByUserId(Long userId){
        return karyawanRepository.findByUserId(userId);
    }

    @Override
    public Karyawan TambahKaryawan(Long userId , Karyawan karyawan){
        // Dapatkan objek User dari UserRepository berdasarkan ID
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            karyawan.setUser(user);
            karyawan.setJabatan(karyawan.getJabatan());
            karyawan.setShift(karyawan.getShift());
            karyawan.setUser(user);
            return karyawanRepository.save(karyawan);
        } else {
            throw new EntityNotFoundException("User dengan ID " + karyawan.getUser().getId() + " tidak ditemukan.");
        }
    }

    @Override
    public Karyawan EditKaryawanByUserId(Long userId, Karyawan karyawan){
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new NotFoundException("User Id Tidak Ditemukan");
        }
        karyawan.setUser(user);
        karyawan.setShift(karyawan.getShift());
        karyawan.setJabatan(karyawan.getJabatan());

        return karyawanRepository.save(karyawan);
    }

    @Override
    public void deleteKaryawan(Long id) {
        if (karyawanRepository.existsById(id)) {
            karyawanRepository.deleteById(id);
        } else {
            throw new NotFoundException("Karyawan not found with id: " + id);
        }
    }

}
