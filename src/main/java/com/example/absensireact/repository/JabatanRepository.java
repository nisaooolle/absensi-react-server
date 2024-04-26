package com.example.absensireact.repository;

import com.example.absensireact.model.Jabatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JabatanRepository extends JpaRepository<Jabatan, Long> {
    // This method assumes there's a method to find by userId and Role in the User repository
    @Query("SELECT j FROM Jabatan j WHERE j.admin.id = :userId AND j.admin.role = com.example.absensireact.role.RoleEnum.ADMIN")
    List<Jabatan> findByUserIdAndAdminRole(Long userId);
}

