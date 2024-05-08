package com.example.absensireact.repository;

import com.example.absensireact.model.Organisasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisasiRepository extends JpaRepository<Organisasi,Long> {


       @Query(value = "SELECT * FROM organisasi WHERE id_admin = :idAdmin" , nativeQuery = true)
       Optional<Organisasi>findOrganisasiByIdAdmin(Long idAdmin);
}
