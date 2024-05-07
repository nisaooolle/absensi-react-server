package com.example.absensireact.service;

import com.example.absensireact.model.Izin;
import com.example.absensireact.model.Lembur;

import java.util.List;
import java.util.Optional;

public interface IzinService {
    List<Izin> getAllIzin();

    Izin getIzinById(Long id);

    List<Izin> getIzinByUserId(Long userId);

    Izin createIzin(Long userId, Izin izin);

    Izin editIzinByid(Long id, Izin izin);

    void deleteIzin(Long id);
}
