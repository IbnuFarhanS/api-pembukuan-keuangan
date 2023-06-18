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

    public Pengguna save(Pengguna pengguna) {
        String encryptedPassword = PasswordEncoderExample.encodePassword(pengguna.getPassword());
        pengguna.setPassword(encryptedPassword);
        return penggunaRepo.save(pengguna);
    }

    public Optional<Pengguna> findById(Long id) {
        return penggunaRepo.findById(id);
    }

    public List<Pengguna> findAll() {
        return penggunaRepo.findAll();
    }

    public void deleteById(Long id) {
        penggunaRepo.delete(id);
    }

    public List<Pengguna> findByNamaPengguna(String namaPengguna) {
        return penggunaRepo.findByNamaPenggunaContains(namaPengguna);
    }

    public Pengguna update(Pengguna pengguna) {
        Pengguna existingPengguna = penggunaRepo.findById(pengguna.getId()).orElse(null);
        if (existingPengguna != null) {
            existingPengguna.setNamaPengguna(pengguna.getNamaPengguna());
            existingPengguna.setEmail(pengguna.getEmail());

            // Cek apakah password berubah
            if (!pengguna.getPassword().equals(existingPengguna.getPassword())) {
                String encryptedPassword = PasswordEncoderExample.encodePassword(pengguna.getPassword());
                existingPengguna.setPassword(encryptedPassword);
            }

            return penggunaRepo.update(existingPengguna);
        } else {
            return null;
        }
    }
}
