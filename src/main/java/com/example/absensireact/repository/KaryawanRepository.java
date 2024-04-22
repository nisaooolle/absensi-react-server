package com.example.absensireact.repository;

import com.example.absensireact.model.Karyawan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan , Long> {
    List<Karyawan>findByUserId(Long userId);
}
