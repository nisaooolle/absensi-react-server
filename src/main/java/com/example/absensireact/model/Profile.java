package com.example.absensireact.model;

import javax.persistence.*;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] picture;

    // Constructor default
    public Profile() {
        // Kosong
    }

    // Constructor dengan parameter
    public Profile(String name, byte[] picture) {
        this.name = name;
        this.picture = picture;
    }

    public Profile(Object o, String name, byte[] bytes) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}

