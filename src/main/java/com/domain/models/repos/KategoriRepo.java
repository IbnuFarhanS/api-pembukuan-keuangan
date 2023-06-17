package com.domain.models.repos;

import com.domain.models.entities.Kategori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class KategoriRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public KategoriRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Kategori> findByNameContains(String name) {
        String sql = "SELECT * FROM tbl_kategori WHERE name LIKE :name";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", "%" + name + "%");
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Kategori.class));
    }

    public Kategori save(Kategori kategori) {
        String sql = "INSERT INTO tbl_kategori (name) VALUES (:name)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", kategori.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder(); // KeyHolder untuk mendapatkan id yang dihasilkan
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"}); // Menggunakan keyHolder dan menentukan kolom id

        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue(); // Mendapatkan id yang dihasilkan
        kategori.setId(generatedId); // Mengatur id yang dihasilkan ke objek kategori

        return kategori;
    }

    // KategoriRepo.java
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM tbl_kategori WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }


    // KategoriRepo.java
    public void delete(Long id) {
        if (existsById(id)) {
            String sql = "DELETE FROM tbl_kategori WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            jdbcTemplate.update(sql, params);
        }
    }

    public List<Kategori> findAll() {
        String sql = "SELECT * FROM tbl_kategori";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Kategori.class));
    }

    public Optional<Kategori> findById(Long id) {
        String sql = "SELECT * FROM tbl_kategori WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Kategori kategori = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Kategori.class));
            return Optional.ofNullable(kategori);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int update(Kategori kategori) {
        String sql = "UPDATE tbl_kategori SET name = :name WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", kategori.getName())
                .addValue("id", kategori.getId());
        return jdbcTemplate.update(sql, params);
    }
}
