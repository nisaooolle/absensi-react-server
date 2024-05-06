package com.example.absensireact.repository;

import com.example.absensireact.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift , Long> {

    @Query(value = "SELECT * FROM shift WHERE organisasi = :organisasi" , nativeQuery = true)
    Optional<Shift> findByOrganisasi (Long organisasi);
}
