package com.domain.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.models.entities.Kategori;
import com.domain.models.repos.KategoriRepo;

@Service
@Transactional
public class KategoriService {

    @Autowired
    private KategoriRepo kategoriRepo;

    public Kategori save(Kategori kategori){
        return kategoriRepo.save(kategori);
    }

    public Kategori findID(Long id){
        Optional<Kategori> kategori = kategoriRepo.findById(id);
        if(!kategori.isPresent()){
            return null;
        }
        return kategori.get();
    }

    public Iterable<Kategori> findAll(){
        return kategoriRepo.findAll();
    }

    public void removeID(Long id){
        kategoriRepo.deleteById(id);
    }

    public List<Kategori> findByName(String name){
        return kategoriRepo.findByNameContains(name);
    }
}
