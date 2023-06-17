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

    public Kategori save(Kategori kategori) {
        return kategoriRepo.save(kategori);
    }

    public Optional<Kategori> findById(Long id) {
        return kategoriRepo.findById(id);
    }

    public List<Kategori> findAll() {
        Iterable<Kategori> kategoriList = kategoriRepo.findAll();
        return StreamSupport.stream(kategoriList.spliterator(), false)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        kategoriRepo.delete(id);
    }

    public List<Kategori> findByName(String name) {
        return kategoriRepo.findByNameContains(name);
    }

    public Kategori update(Kategori kategori) {
        int rowsAffected = kategoriRepo.update(kategori);
        if (rowsAffected > 0) {
            return kategori;
        } else {
            return null;
        }
    }
}
