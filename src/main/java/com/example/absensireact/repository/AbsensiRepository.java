package com.example.absensireact.repository;

import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AbsensiRepository extends JpaRepository<Absensi , Long> {

    Optional<Absensi> findByUserAndTanggalAbsen(User user, Date tanggalAbsen);

    List<Absensi> findByUser_Id(Long userId);

    @Query(value = "SELECT * FROM absensi WHERE user_id = :userId" , nativeQuery = true)
    Optional<Absensi>findByUserId (Long userId);
    @Query(value = "SELECT * FROM absensi WHERE user_id = :userId" , nativeQuery = true)
    List<Absensi>findabsensiByUserId (Long userId);

    @Query(value = "SELECT * FROM absensi WHERE user_id = :userId AND tanggal_absen = :tanggalAbsen", nativeQuery = true)
    Optional<Absensi> findByUserIdAndTanggalAbsen(Long userId, Date tanggalAbsen);

}
