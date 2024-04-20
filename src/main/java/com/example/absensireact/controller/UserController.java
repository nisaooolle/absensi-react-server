package com.example.absensireact.controller;


import com.example.absensireact.model.LoginRequest;
import com.example.absensireact.exception.CommonResponse;
import com.example.absensireact.exception.ResponseHelper;
import com.example.absensireact.impl.UserImpl;
import com.example.absensireact.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserImpl userImpl;


    @PostMapping("/login")
    public CommonResponse<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return ResponseHelper.ok( userImpl.login(loginRequest));
    }

    @PostMapping("/user/register")
    public ResponseEntity<User> register(@RequestBody User user){

        return ResponseEntity.ok( userImpl.Register(user));
    }

    @GetMapping("/user/get-allUser")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userImpl.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/getUserBy/{id}")
    public ResponseEntity<User> GetUserById (@PathVariable Long id){
        User user = userImpl.getById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")  Long id) {
        return ResponseEntity.ok( userImpl.delete(id));
    }

}
