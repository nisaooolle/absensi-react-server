package com.example.absensireact.service;

import com.example.absensireact.model.Admin;
import com.example.absensireact.model.LoginRequest;
import com.example.absensireact.model.User;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin RegisterBySuperAdmin(Long idSuperAdmin, Admin admin);

    Admin RegisterAdmin(Admin admin);

    Admin getById(Long id);

    List<Admin>getAllBySuperAdmin(Long idSuperAdmin);

    List<Admin> getAll();

    Admin edit(Long id, Admin admin);

    Map<String, Boolean> delete(Long id);
}
