package com.example.absensireact.detail;

import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

public class SuperAdminDetail implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String password;
    private  String username;

    private  String imageAdmin;
    private String role;


    public SuperAdminDetail(Long id, String email, String password, String username, String imageAdmin, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.imageAdmin = imageAdmin;
        this.role = role;
    }

    public static SuperAdminDetail buildSuperAdmin(SuperAdmin superAdmin) {
        return new SuperAdminDetail(
               superAdmin.getId(),
                superAdmin.getEmail(),
                superAdmin.getPassword(),
                superAdmin.getUsername(),
                superAdmin.getImageAdmin(),
                "SUPERADMIN"

        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageAdmin() {
        return imageAdmin;
    }

    public void setImageAdmin(String imageAdmin) {
        this.imageAdmin = imageAdmin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("SUPERADMIN"));
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
