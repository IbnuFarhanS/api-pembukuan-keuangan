package com.domain.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<Kategori> findAll() {
        return kategoriService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Kategori> kategori = kategoriService.findById(id);
        if (kategori.isPresent()) {
            return ResponseEntity.ok(kategori.get());
        } else {
            String errorMessage = "Data dengan ID " + id + " tidak ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<?> findByNama(@PathVariable String name) {
        try {
            return ResponseEntity.ok(kategoriService.findByNameContains(name));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Kategori kategori) {
        try {
            Kategori createdKategori = kategoriService.save(kategori);
            return ResponseEntity.ok(createdKategori);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Kategori kategori) {
    Kategori existingKategori = kategoriService.findById(id).orElse(null);
    if (existingKategori != null) {
        String name = kategori.getName();
        if (kategoriService.existsByName(name) && !name.equals(existingKategori.getName())) {
            String errorMessage = "Nama kategori '" + name + "' sudah digunakan.";
            return ResponseEntity.badRequest().body(errorMessage);
        }
        existingKategori.setName(name);
        Kategori updatedKategori = kategoriService.update(existingKategori);
        if (updatedKategori != null) {
            return ResponseEntity.ok(updatedKategori);
        } else {
            String errorMessage = "Gagal memperbarui kategori.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    } else {
        String errorMessage = "Data dengan ID " + id + " tidak ditemukan.";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        kategoriService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }
}
