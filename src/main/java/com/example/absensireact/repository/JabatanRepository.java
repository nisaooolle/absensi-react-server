package com.example.absensireact.repository;

import com.example.absensireact.model.Jabatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JabatanRepository extends JpaRepository<Jabatan, Long> {
    List<Jabatan> findByAdminId(Long adminId);
}
