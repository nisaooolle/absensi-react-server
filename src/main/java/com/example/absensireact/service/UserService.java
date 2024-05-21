package com.example.absensireact.service;

import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {

//    Map<Object, Object> login(LoginRequest loginRequest, HttpServletRequest request);

//    User Register(User user);


    User Register(User user, Long idOrganisasi);

    List<User> GetAllKaryawanByIdAdmin(Long idAdmin);

    User Tambahkaryawan(Long idAdmin, User user);

    User getById(Long id);

    List<User> getAll();


    User fotoUser(Long id, MultipartFile image) throws  IOException;

    User edit(Long id, User user);

    void delete(Long id) throws IOException;

    void deleteUser(Long id);
}
