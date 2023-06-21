package com.domain.controllers;

import com.domain.helpers.password.PasswordEncoderExample;
import com.domain.models.entities.Pengguna;
import com.domain.services.PenggunaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pengguna")
public class PenggunaController {

    private final PenggunaService penggunaService;

    @Autowired
    public PenggunaController(PenggunaService penggunaService) {
        this.penggunaService = penggunaService;
    }

    // ============================== FIND ALL ID ====================================
    @GetMapping
    public List<Pengguna> findAll() {
        return penggunaService.findAll();
    }

    // ============================== FIND BY ID ====================================
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Pengguna> pengguna = penggunaService.findById(id);
        if (pengguna.isPresent()) {
            return ResponseEntity.ok(pengguna.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Data dengan ID " + id + " tidak ditemukan.");
        }
    }

    // ============================== FIND BY NAMA PENGGUNA ====================================
    @GetMapping("/findByNamaPengguna/{namaPengguna}")
    public ResponseEntity<?> findByNamaPengguna(@PathVariable String namaPengguna) {
        try {
            return ResponseEntity.ok(penggunaService.findByNamaPenggunaContains(namaPengguna));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ============================== FIND BY USERNAME ====================================
    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        try {
            Pengguna pengguna = penggunaService.findByUsername(username);
            return ResponseEntity.ok(pengguna);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ============================== SAVE ====================================
    @PostMapping
    public ResponseEntity<Pengguna> save(@RequestBody Pengguna pengguna) {
        Pengguna createdPengguna = penggunaService.save(pengguna);
        String encryptedPassword = PasswordEncoderExample.encodePassword(createdPengguna.getPassword());
        createdPengguna.setPassword(encryptedPassword);
        return ResponseEntity.ok(createdPengguna);
    }

    // ============================== UPDATE ====================================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Pengguna pengguna) {
        Pengguna existingPengguna = penggunaService.findById(id).orElse(null);
        if (existingPengguna != null) {
            existingPengguna.setNamaPengguna(pengguna.getNamaPengguna());
            existingPengguna.setUsername(pengguna.getUsername());
            existingPengguna.setEmail(pengguna.getEmail());

            // Cek apakah password berubah
            if (!pengguna.getPassword().equals(existingPengguna.getPassword())) {
                existingPengguna.setPassword(pengguna.getPassword());
            }

            Pengguna updatedPengguna = penggunaService.update(existingPengguna);
            return ResponseEntity.ok(updatedPengguna);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Data dengan ID " + id + " tidak ditemukan.");
        }
    }

    // ============================== DELETE ====================================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        penggunaService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }
}