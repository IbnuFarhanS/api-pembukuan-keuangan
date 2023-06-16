package com.domain.models.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.domain.models.entities.Kategori;

public interface KategoriRepo extends CrudRepository<Kategori, Long>{

    List<Kategori> findByNameContains(String name);
}
