package com.example.absensireact.service;

import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface SuperAdminService {
    SuperAdmin getAllSuperAdmin();

    Optional<SuperAdmin> getSuperadminbyId(Long id);


    SuperAdmin RegisterSuperAdmin(SuperAdmin superAdmin);

    SuperAdmin tambahSuperAdmin(Long id, SuperAdmin superAdmin, MultipartFile image);

    SuperAdmin EditSuperAdmin(Long id, MultipartFile image, SuperAdmin superAdmin);

    void deleteSuperAdmin(Long id);
}
