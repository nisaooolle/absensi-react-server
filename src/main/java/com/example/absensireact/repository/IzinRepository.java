package com.example.absensireact.repository;

import com.example.absensireact.model.Izin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IzinRepository extends JpaRepository<Izin , Long> {
}
