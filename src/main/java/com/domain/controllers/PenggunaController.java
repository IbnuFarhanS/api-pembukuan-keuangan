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

    @GetMapping("/findByNamaPengguna/{namaPengguna}")
    public ResponseEntity<?> findByNamaPengguna(@PathVariable String namaPengguna) {
        try {
            return ResponseEntity.ok(penggunaService.findByNamaPenggunaContains(namaPengguna));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Pengguna> create(@RequestBody Pengguna pengguna) {
        Pengguna createdPengguna = penggunaService.save(pengguna);
        String encryptedPassword = PasswordEncoderExample.encodePassword(createdPengguna.getPassword());
        createdPengguna.setPassword(encryptedPassword);
        return ResponseEntity.ok(createdPengguna);
    }

    @GetMapping
    public List<Pengguna> findAll() {
        return penggunaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pengguna> findById(@PathVariable("id") Long id) {
        Optional<Pengguna> pengguna = penggunaService.findById(id);
        return pengguna.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Pengguna update(@PathVariable("id") Long id, @RequestBody Pengguna pengguna) {
        Pengguna existingPengguna = penggunaService.findById(id).orElse(null);
        if (existingPengguna != null) {
            existingPengguna.setNamaPengguna(pengguna.getNamaPengguna());
            existingPengguna.setEmail(pengguna.getEmail());

            // Cek apakah password berubah
            if (!pengguna.getPassword().equals(existingPengguna.getPassword())) {
                existingPengguna.setPassword(pengguna.getPassword());
            }

            return penggunaService.update(existingPengguna);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        penggunaService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }
}