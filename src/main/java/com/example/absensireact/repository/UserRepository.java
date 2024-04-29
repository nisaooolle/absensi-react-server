package com.example.absensireact.repository;


import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByIdAndRole(Long id, String role);

    User findByEmail(String email);
    @Query(value = "SELECT * FROM user WHERE email = :email" , nativeQuery = true)
    Optional<User> findByEmailUser (String email);
    Boolean existsByEmail(String email);

}
