package com.example.absensireact.repository;

import com.example.absensireact.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Long> {

    Admin findByEmail(String email);
    Boolean existsByEmail(String email);

}
