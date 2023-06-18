package com.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.models.entities.Kategori;
import com.domain.models.repos.KategoriRepo;

@Service
@Transactional
public class KategoriService {

    private final KategoriRepo kategoriRepo;

    @Autowired
    public KategoriService(KategoriRepo kategoriRepo) {
        this.kategoriRepo = kategoriRepo;
    }

    // ============================== FIND ALL ID ====================================
    public List<Kategori> findAll() {
        Iterable<Kategori> kategoriList = kategoriRepo.findAll();
        return StreamSupport.stream(kategoriList.spliterator(), false)
                .collect(Collectors.toList());
    }

    // ============================== FIND BY ID ====================================
    public Optional<Kategori> findById(Long id) {
        return kategoriRepo.findById(id);
    }

    // ============================== FIND BY NAME ====================================
    public List<Kategori> findByNameContains(String name) {
        List<Kategori> kategoris = kategoriRepo.findByNameContains(name);
        if (kategoris.isEmpty()) {
            throw new IllegalArgumentException("Kategori '" + name + "'  tidak ditemukan.");
        }
        return kategoris;
    }

    // ============================== SAVE ====================================
    public Kategori save(Kategori kategori) {
        String name = kategori.getName();
        if (kategoriRepo.existsByName(name)) {
            throw new IllegalArgumentException("Nama kategori '" + name + "' sudah digunakan.");
        }
        return kategoriRepo.save(kategori);
    }

    // ============================== UPDATE ====================================
    public Kategori update(Kategori kategori) {
        Optional<Kategori> existingKategori = kategoriRepo.findById(kategori.getId());
        if (existingKategori.isPresent()) {
            String name = kategori.getName();
            if (kategoriRepo.existsByName(name) && !existingKategori.get().getName().equals(name)) {
                throw new IllegalArgumentException("Nama kategori '" + name + "' sudah digunakan.");
            }

            int rowsAffected = kategoriRepo.update(kategori);
            if (rowsAffected > 0) {
                return kategori;
            } else {
                throw new IllegalStateException("Gagal memperbarui kategori.");
            }
        } else {
            throw new IllegalArgumentException("Data kategori dengan ID " + kategori.getId() + " tidak ditemukan.");
        }
    }

    // ============================== DELETE ====================================
    public void deleteById(Long id) {
        kategoriRepo.delete(id);
    }

    // ============================== EXISTS BY NAME ====================================
    public boolean existsByName(String name) {
        return kategoriRepo.existsByName(name);
    }
}
