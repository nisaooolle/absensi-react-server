package com.example.absensireact.detail;

import com.example.absensireact.model.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;

public class AdminDetail implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    private String email;
    private String password;
    private  String username;
    private  String imageAdmin;
    private String role;
    private String idOrganisasi;

    public AdminDetail(Long id, String username,String email ,String password, String imageAdmin, String role, String idOrganisasi) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.imageAdmin = imageAdmin;
        this.role = role;
        this.idOrganisasi = idOrganisasi;
    }


    public static AdminDetail buildAdmin(Admin admin) {
        return new AdminDetail(
                admin.getId(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getUsername(),
                admin.getImageAdmin(),
                "ADMIN",
                admin.getIdOrganisasi()
        );
     }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ADMIN"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageAdmin() {
        return imageAdmin;
    }

    public void setImageAdmin(String imageAdmin) {
        this.imageAdmin = imageAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIdOrganisasi() {
        return idOrganisasi;
    }

    public void setIdOrganisasi(String idOrganisasi) {
        this.idOrganisasi = idOrganisasi;
    }

    @Override
    public String getPassword() {
        return   password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    }