package com.example.absensireact.repository;


import com.example.absensireact.model.Cuti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CutiRepository extends JpaRepository<Cuti ,Long> {

    List<Cuti> findByUserId(Long userId);

}