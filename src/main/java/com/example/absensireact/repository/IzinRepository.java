package com.example.absensireact.repository;

import com.example.absensireact.model.Izin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IzinRepository extends JpaRepository<Izin , Long> {

    @Query(value = "SELECT * FROM izin WHERE user_id = :userId" , nativeQuery = true)
    List<Izin> findByUserId(Long userId);
}
