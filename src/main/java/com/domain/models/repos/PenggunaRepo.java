package com.domain.models.repos;

import com.domain.models.entities.Pengguna;
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
public class PenggunaRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PenggunaRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================== FIND ALL ID ====================================
    public List<Pengguna> findAll() {
        String sql = "SELECT * FROM pengguna";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Pengguna.class));
    }

    // ============================== FIND BY ID ====================================
    public Optional<Pengguna> findById(Long id) {
        String sql = "SELECT * FROM pengguna WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Pengguna pengguna = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Pengguna.class));
            return Optional.ofNullable(pengguna);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // ============================== FIND BY NAMA PENGGUNA ====================================
    public List<Pengguna> findByNamaPenggunaContains(String namaPengguna) {
        String sql = "SELECT * FROM pengguna WHERE nama_pengguna LIKE :namaPengguna";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("namaPengguna", "%" + namaPengguna + "%");

        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Pengguna.class));
    }

    // ============================== FIND BY USERNAME ====================================
    public Pengguna findByUsername(String username) {
        String sql = "SELECT * FROM pengguna WHERE username = :username";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", username);

        try {
            return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Pengguna.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // ============================== SAVE ====================================
    public Pengguna save(Pengguna pengguna) {
        String sql = "INSERT INTO pengguna (nama_pengguna, username, email, password) VALUES (:namaPengguna, :username, :email, :password)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("namaPengguna", pengguna.getNamaPengguna())
                .addValue("username", pengguna.getUsername())
                .addValue("email", pengguna.getEmail())
                .addValue("password", pengguna.getPassword());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        pengguna.setId(generatedId);

        return pengguna;
    }

    // ============================== UPDATE ====================================
    public Pengguna update(Pengguna pengguna) {
        String sql = "UPDATE pengguna SET nama_pengguna = :namaPengguna, username = :username, email = :email, password = :password WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("namaPengguna", pengguna.getNamaPengguna())
                .addValue("username", pengguna.getUsername())
                .addValue("email", pengguna.getEmail())
                .addValue("password", pengguna.getPassword())
                .addValue("id", pengguna.getId());

        jdbcTemplate.update(sql, params);

        return pengguna;
    }

    // ============================== DELETE ====================================
    public void delete(Long id) {
        String sql = "DELETE FROM pengguna WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, params);
    }
}
