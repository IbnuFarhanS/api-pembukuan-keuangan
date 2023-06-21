package com.domain.services;

import com.domain.helpers.password.PasswordEncoderExample;
import com.domain.models.entities.Pengguna;
import com.domain.models.repos.PenggunaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PenggunaService {

    private final PenggunaRepo penggunaRepo;

    @Autowired
    public PenggunaService(PenggunaRepo penggunaRepo) {
        this.penggunaRepo = penggunaRepo;
    }

    // ============================== FIND ALL ID ====================================
    public List<Pengguna> findAll() {
        return penggunaRepo.findAll();
    }

    // ============================== FIND BY ID ====================================
    public Optional<Pengguna> findById(Long id) {
        return penggunaRepo.findById(id);
    }

    // ============================== FIND BY NAMA PENGGUNA ====================================
    public List<Pengguna> findByNamaPenggunaContains(String namaPengguna) {
        List<Pengguna> penggunas = penggunaRepo.findByNamaPenggunaContains(namaPengguna);
        if (penggunas.isEmpty()) {
            throw new IllegalArgumentException("Pengguna '" + namaPengguna + "'  tidak ditemukan.");
        }
        return penggunas;
    }

    // ============================== FIND BY USERNAME ====================================
    public Pengguna findByUsername(String username) {
        Pengguna pengguna = penggunaRepo.findByUsername(username);
        if (pengguna == null) {
            throw new IllegalArgumentException("Pengguna '" + username + "' tidak ditemukan.");
        }
        return pengguna;
    }

    // ============================== SAVE ====================================
    public Pengguna save(Pengguna pengguna) {
        String username = pengguna.getUsername();
        if (penggunaRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("Username pengguna '" + username + "' sudah digunakan.");
        }
        String encryptedPassword = PasswordEncoderExample.encodePassword(pengguna.getPassword());
        pengguna.setPassword(encryptedPassword);
        return penggunaRepo.save(pengguna);
    }

    // ============================== UPDATE ====================================
    public Pengguna update(Pengguna pengguna) {
        Optional<Pengguna> existingPenggunaOpt = penggunaRepo.findById(pengguna.getId());
        if (existingPenggunaOpt.isPresent()) {
            Pengguna existingPengguna = existingPenggunaOpt.get();
            String newUsername = pengguna.getUsername();

            // Check if new username already exists for other users
            if (penggunaRepo.existsByUsername(newUsername) && !existingPengguna.getUsername().equals(newUsername)) {
                throw new IllegalArgumentException("Username pengguna '" + newUsername + "' sudah digunakan.");
            }

            existingPengguna.setNamaPengguna(pengguna.getNamaPengguna());
            existingPengguna.setUsername(newUsername);
            existingPengguna.setEmail(pengguna.getEmail());

            // Check if password has changed
            if (!pengguna.getPassword().equals(existingPengguna.getPassword())) {
                String encryptedPassword = PasswordEncoderExample.encodePassword(pengguna.getPassword());
                existingPengguna.setPassword(encryptedPassword);
            }

            Pengguna updatedPengguna = penggunaRepo.update(existingPengguna);
            return updatedPengguna;
        } else {
            throw new IllegalArgumentException("Pengguna dengan ID '" + pengguna.getId() + "' tidak ditemukan.");
        }
    }

    // ============================== DELETE ====================================
    public void deleteById(Long id) {
        penggunaRepo.delete(id);
    }

    // ============================== EXISTS BY USERNNAME ====================================
    public boolean existsByUsername(String username) {
        return penggunaRepo.existsByUsername(username);
    }
}
