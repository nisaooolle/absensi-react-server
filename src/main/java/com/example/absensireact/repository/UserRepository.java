package com.example.absensireact.repository;


import com.example.absensireact.model.User;
import com.example.absensireact.role.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByIdAndRole(Long id, String role);

    User findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByIdAndRole(Long id, RoleEnum role);

}
