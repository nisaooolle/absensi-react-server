package com.example.absensireact.repository;

import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Cuti;
import com.example.absensireact.model.Karyawan;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan , Long> {
    @Query(value = "SELECT * FROM karyawan WHERE admin_id = :adminId", nativeQuery = true)
    Optional<Karyawan> findByAdmin(Long adminId);
   @Query(value = "SELECT * FROM karyawan WHERE admin_id = :adminId", nativeQuery = true)
    Optional<Karyawan> findKaryawanByadmin(Admin adminId);



}
