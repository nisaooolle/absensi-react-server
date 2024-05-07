package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Izin;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.IzinRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.IzinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class IzinImpl implements IzinService {
    @Autowired
    private IzinRepository izinRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Izin> getAllIzin() {
        return izinRepository.findAll();
    }

    @Override
    public Izin getIzinById(Long id) {
        Optional<Izin> izinOptional = izinRepository.findById(id);
        return izinOptional.orElse(null);
    }

    @Override
    public List<Izin> getIzinByUserId(Long userId){
      return izinRepository.findByUserId(userId);
     }

    @Override
    public Izin createIzin(Long userId, Izin izin) {
        User userIzin = userRepository.findById(userId).orElse(null);
        if (userIzin == null) {
            throw new NotFoundException("User id tidak ditemukan");
        }
        LocalDate tanggal = LocalDate.now();
        izin.setUser(userIzin);
        izin.setKeternganIzin(izin.getKeternganIzin());
        izin.setTanggalIzin(tanggal);
        return izinRepository.save(izin);
    }

    @Override
    public Izin editIzinByid(Long id, Izin izin){
        Izin izincek = izinRepository.findById(id).orElse(null);
        if (izincek == null) {
            throw new NotFoundException("Izin tidak ditemukan");
        }
        izin.setKeternganIzin(izin.getKeternganIzin());
        return izinRepository.save(izin);

    }
    @Override
    public void deleteIzin(Long id) {
        izinRepository.deleteById(id);
    }
}
