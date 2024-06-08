package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Shift;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.ShiftRepository;
import com.example.absensireact.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

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
    public Shift editShiftById(Long id, Long idAdmin, Shift updatedShift) {
        Optional<Admin> adminOptional = adminRepository.findById(idAdmin);
        if (adminOptional.isEmpty()) {
            throw new NotFoundException("id admin tidak ditemukan");
        }
        Optional<Shift> shiftOptional = shiftRepository.findById(id);
        if (shiftOptional.isEmpty()) {
            throw new NotFoundException("Shift id tidak ditemukan");
        }
        Shift shift = shiftOptional.get();
        Admin admin = adminOptional.get();

        shift.setAdmin(admin);
        shift.setNamaShift(updatedShift.getNamaShift());
        shift.setWaktuMasuk(updatedShift.getWaktuMasuk());
        shift.setWaktuPulang(updatedShift.getWaktuPulang());

        return shiftRepository.save(shift);
    }
    @Override
    public Map<String, Boolean> delete(Long id) {
        try {
            List<User> users = userRepository.findByIdShift(id);
            for (User user : users) {
                user.setShift(null);
                userRepository.save(user);
            }

            shiftRepository.deleteById(id);

            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.FALSE);
            return res;
        }
    }
}
