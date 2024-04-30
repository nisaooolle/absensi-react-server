package com.example.absensireact.service;

import com.example.absensireact.model.Izin;
import com.example.absensireact.model.Lembur;

import java.util.List;

public interface IzinService {
    List<Izin> getAllIzin();

    Izin getIzinById(Long id);

    Izin createIzin(Long userId, Izin izin);

    Izin editIzinByid(Long id, Izin izin);

    void deleteIzin(Long id);
}
