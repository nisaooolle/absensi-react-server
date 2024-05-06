package com.example.absensireact.service;

import com.example.absensireact.model.Shift;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ShiftService {
    List<Shift> getAllShift();

    Optional<Shift> getshiftById(Long id);

    Optional<Shift>getByOrganisasi(Long organisasi);

    Shift PostShift(Long organisasi, Shift shift);

    Shift editShiftById(Long id, Shift shift);

    Map<String, Boolean> delete(Long id);
}
