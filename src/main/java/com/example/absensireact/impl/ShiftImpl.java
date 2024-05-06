package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.Shift;
import com.example.absensireact.repository.OrganisasiRepository;
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
    private OrganisasiRepository organisasiRepository;

    @Override
    public List<Shift> getAllShift(){
        return shiftRepository.findAll();
    }

    @Override
    public Optional<Shift>getshiftById(Long id){
        return shiftRepository.findById(id);
    }

    @Override
    public Optional<Shift>getByOrganisasi(Long organisasi) {
        return shiftRepository.findByOrganisasi(organisasi);

    }

    @Override
    public Shift PostShift(Long organisasi, Shift shift){
        Organisasi organisasi1 = organisasiRepository.findById(organisasi).orElse(null);
        if (organisasi1 != null) {
            shift.setNamaShift(shift.getNamaShift());
            shift.setWaktuMasuk(shift.getWaktuMasuk());
            shift.setWaktuPulang(shift.getWaktuPulang());
            shift.setJumlahKaryawan(shift.getJumlahKaryawan());
            shift.setOrganisasi(organisasi1);
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
        shift.setJumlahKaryawan(shift.getJumlahKaryawan());
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
