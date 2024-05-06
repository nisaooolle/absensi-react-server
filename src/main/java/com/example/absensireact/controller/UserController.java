package com.example.absensireact.controller;


import com.example.absensireact.config.AppConfig;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.OrganisasiRepository;
import com.example.absensireact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userImpl;

    @Autowired
    OrganisasiRepository organisasiRepository;



    @Autowired
    private AppConfig appConfig;

//    @PostMapping("/login")
//    public Map<Object, Object> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
//        return userImpl.login(loginRequest, request);
//    }

    @PostMapping("/user/register")
    public ResponseEntity<User> registerUser(@RequestBody User user, @RequestParam Long organisasiId) {
        Organisasi organisasi = organisasiRepository.findById(organisasiId)
                .orElseThrow(() -> new NotFoundException("Organisasi tidak ditemukan"));

        User newUser = userImpl.Register(user, organisasi);

        return ResponseEntity.ok(newUser);
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
