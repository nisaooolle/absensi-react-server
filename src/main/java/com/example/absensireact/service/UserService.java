package com.example.absensireact.service;

import com.example.absensireact.model.LoginRequest;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {

//    Map<Object, Object> login(LoginRequest loginRequest, HttpServletRequest request);

//    User Register(User user);


    User Register(User user, Organisasi organisasi);

    User getById(Long id);

    List<User> getAll();

    User edit(Long id, User user);


    Map<String, Boolean> delete(Long id);
}
