package com.domain.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "tbl_kategori")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Kategori() {
    }

    public Kategori(String name) {
        this.name = name;
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

    public Kategori orElse(Object object) {
        return null;
    }
}
