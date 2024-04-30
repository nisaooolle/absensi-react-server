package com.example.absensireact.model;



import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "email")
   private String email;

   @Column(name = "password" , unique = true)
   private String password;

   @Column(name = "username")
   private  String username;

   @Column(name = "organisasi"  )
   private  String organisasi;


   @Column(name = "role")
   private String role;

public User(){

}

   public User(Long id, String email, String password, String username, String organisasi, String role) {
       this.id = id;
       this.email = email;
       this.password = password;
       this.username = username;
       this.organisasi = organisasi;
       this.role = role;
   }

   public User(String email, String password, List<SimpleGrantedAuthority> roles) {
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

   public String getPassword() {
       return password;
   }

   public void setPassword(String password) {
       this.password = password;
   }


   public String getUsername() {
       return username;
   }

   public void setUsername(String username) {
       this.username = username;
   }

   public String getOrganisasi() {
       return organisasi;
   }

   public void setOrganisasi(String organisasi) {
       this.organisasi = organisasi;
   }

   public String getRole() {
       return role;
   }

   public void setRole(String role) {
       this.role = role;
   }


   public void setOrganisasiId(Long organisasiId) {
   }

   public void setJabatanId(Long jabatanId) {
   }
}
