package com.domain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.domain.models.entities.Kategori;
import com.domain.services.KategoriService;

@RestController
@RequestMapping("/api/kategori")
public class KategoriController {

    @Autowired
    private KategoriService kategoriService;

    @PostMapping
    public Kategori create(@RequestBody Kategori kategori){
        return kategoriService.save(kategori);
    }

    @GetMapping
    public Iterable<Kategori> findAll(){
        return kategoriService.findAll();
    }

    @GetMapping("/{id}")
    public Kategori findID(@PathVariable("id") Long id){
        return kategoriService.findID(id);
    }

    @PutMapping
    public Kategori update(@RequestBody Kategori kategori){
        return kategoriService.save(kategori);
    }

    @DeleteMapping("/{id}")
    public void removeID(@PathVariable("id") Long id){
        kategoriService.removeID(id);
    }
}
