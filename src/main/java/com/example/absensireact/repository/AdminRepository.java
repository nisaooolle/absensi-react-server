package com.example.absensireact.repository;

import com.example.absensireact.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Long> {

    Optional<Admin> findByEmail(String email);
    Boolean existsByEmail(String email);

}
