package com.example.absensireact.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
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


   @ManyToOne
   @JoinColumn(name = "idOrganisasi")
   private Organisasi organisasi;

   @ManyToOne
   @JoinColumn(name = "idJabatan")
   private Jabatan jabatan;

   @ManyToOne
   @JoinColumn(name = "idShift")
   private Shift shift;

   @ManyToOne
   @JoinColumn(name = "idAdmin")
   private Admin admin;


   @Column(name = "role")
   private String role;

public User(){

}

    public User(Long id, String email, String password, String username, Organisasi organisasi, Jabatan jabatan, Shift shift, Admin admin, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.organisasi = organisasi;
        this.jabatan = jabatan;
        this.shift = shift;
        this.admin = admin;
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



   public String getRole() {
       return role;
   }

   public void setRole(String role) {
       this.role = role;
   }

    public Organisasi getOrganisasi() {
        return organisasi;
    }

    public void setOrganisasi(Organisasi organisasi) {
        this.organisasi = organisasi;
    }

    public Jabatan getJabatan() {
        return jabatan;
    }

    public void setJabatan(Jabatan jabatan) {
        this.jabatan = jabatan;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
