package com.domain.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private final KategoriService kategoriService;

    @Autowired
    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
    }

    @PostMapping
    public Kategori create(@RequestBody Kategori kategori) {
        return kategoriService.save(kategori);
    }

    @GetMapping
    public List<Kategori> findAll() {
        return kategoriService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kategori> findID(@PathVariable("id") Long id) {
        Optional<Kategori> kategori = kategoriService.findID(id);
        return kategori.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Kategori update(@PathVariable("id") Long id, @RequestBody Kategori kategori) {
        Kategori existingKategori = kategoriService.findID(id).orElse(null);
        if (existingKategori != null) {
            existingKategori.setName(kategori.getName());
            return kategoriService.update(existingKategori);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        kategoriService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }
}
