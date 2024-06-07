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


    List<User> getAllByJabatan(Long idJabatan);

    User editUsernameJabatanShift(Long id, Long idJabatan, Long idShift, User user);

    User Tambahkaryawan(User user, Long idAdmin, Long idOrganisasi, Long idJabatan, Long idShift);

    List<User> GetAllKaryawanByIdAdmin(Long idAdmin);


    User getById(Long id);

    List<User> getAll();


    User fotoUser(Long id, MultipartFile image) throws  IOException;

    User edit(Long id, User user);

    void delete(Long id) throws IOException;

    void deleteUser(Long id);
}
