package com.example.absensireact.repository;

import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsensiRepository extends JpaRepository<Absensi , Long> {


    List<Absensi> findByUser_Id(Long userId);


}
