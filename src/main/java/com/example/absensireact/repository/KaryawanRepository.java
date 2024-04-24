package com.example.absensireact.repository;

import com.example.absensireact.model.Karyawan;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan , Long> {
    List<Karyawan>findByUserId(Long userId);
    Karyawan findJabatanByuserId(Long userId);
    Optional<Karyawan> findByUser(User user);


}
