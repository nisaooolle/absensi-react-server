package com.example.absensireact.repository;

import com.example.absensireact.model.Organisasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisasiRepository extends JpaRepository<Organisasi,Long> {

    List<Organisasi>GetallById(Long id);

    List<Organisasi>GetAllByIdAdmin(Long idAdmin);
}
