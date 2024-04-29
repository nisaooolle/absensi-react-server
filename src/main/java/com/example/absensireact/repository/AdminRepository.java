package com.example.absensireact.repository;

import com.example.absensireact.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Long> {

    Admin findByEmail(String email);
    @Query(value = "SELECT * FROM admin WHERE email = :email" , nativeQuery = true)
    Optional<Admin> findByAdminEmail (String email);
    Boolean existsByEmail(String email);
    Optional<Admin> findByIdAndRole(Long id, String role);


}
