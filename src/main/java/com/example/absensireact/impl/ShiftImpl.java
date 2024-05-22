package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Shift;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.ShiftRepository;
import com.example.absensireact.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShiftImpl implements ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Shift> getAllShift(){
        return shiftRepository.findAll();
    }

    @Override
    public List<Shift> getAllShiftByAdmin(Long idAdmin){
        return shiftRepository.getByIdAdmin(idAdmin);
    }


    @Override
    public Optional<Shift>getshiftById(Long id){
        return shiftRepository.findById(id);
    }

    @Override
    public Optional<Shift>getbyAdmin(Long idAdmin) {
        return shiftRepository.findByIdAdmin(idAdmin);

    }

    @Override
    public Shift PostShift(Long idAdmin, Shift shift){
        Optional<Admin> admin1 = adminRepository.findById(idAdmin);
        if (admin1.isPresent()) {
            Admin admin = admin1.get();
            shift.setNamaShift(shift.getNamaShift());
            shift.setWaktuMasuk(shift.getWaktuMasuk());
            shift.setWaktuPulang(shift.getWaktuPulang());
            shift.setAdmin(admin);
            return shiftRepository.save(shift);
        }
       throw new NotFoundException("Organisasi tidak ditemukan ");
    }

    @Override
    public Shift editShiftById(Long id, Shift shift){
        Optional<Shift> shiftValidate = Optional.ofNullable(shiftRepository.findById(id).orElse(null));
        if (!shiftValidate.isPresent()) {
        throw new NotFoundException("Shift id tidak ditemukan");
        }
        shift.setNamaShift(shift.getNamaShift());
        shift.setWaktuMasuk(shift.getWaktuMasuk());
        shift.setWaktuPulang(shift.getWaktuPulang());
        return shiftRepository.save(shift);
    }
    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            shiftRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            return null;
        }
    }
}
