package com.domain.models.repos;

import com.domain.models.entities.DaftarKeuangan;
import com.domain.models.entities.Kategori;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaftarKeuanganRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DaftarKeuanganRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DaftarKeuangan save(DaftarKeuangan daftarKeuangan) {
        String sql = "INSERT INTO daftar_keuangan (kategori_id, amount, date) VALUES (:kategoriId, :amount, :date)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("kategoriId", daftarKeuangan.getKategori().getId())
                .addValue("amount", daftarKeuangan.getAmount())
                .addValue("date", daftarKeuangan.getDate());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = keyHolder.getKey().longValue();
        daftarKeuangan.setId(generatedId);

        return daftarKeuangan;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM daftar_keuangan WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    public void delete(Long id) {
        if (existsById(id)) {
            String sql = "DELETE FROM daftar_keuangan WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            jdbcTemplate.update(sql, params);
        }
    }

    public List<DaftarKeuangan> findAll() {
        String sql = "SELECT dk.id, dk.amount, dk.date, k.id as kategori_id, k.name as kategori_name " +
                     "FROM daftar_keuangan dk " +
                     "JOIN tbl_kategori k ON dk.kategori_id = k.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));

            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
    }

    public DaftarKeuangan findById(Long id) {
        String sql = "SELECT dk.id, dk.amount, dk.date, k.id as kategori_id, k.name as kategori_name " +
                     "FROM daftar_keuangan dk " +
                     "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                     "WHERE dk.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
    
        List<DaftarKeuangan> result = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());
    
            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
    
            daftarKeuangan.setKategori(kategori);
    
            return daftarKeuangan;
        });
    
        return result.isEmpty() ? null : result.get(0);
    }
    

    public int update(DaftarKeuangan daftarKeuangan) {
        String sql = "UPDATE daftar_keuangan SET kategori_id = :kategoriId, amount = :amount, date = :date WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("kategoriId", daftarKeuangan.getKategori())
                .addValue("amount", daftarKeuangan.getAmount())
                .addValue("date", daftarKeuangan.getDate())
                .addValue("id", daftarKeuangan.getId());
        return jdbcTemplate.update(sql, params);
    }

    public List<DaftarKeuangan> findByKategoriId(Long kategoriId) {
        String sql = "SELECT * FROM daftar_keuangan WHERE kategori_id = :kategoriId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("kategoriId", kategoriId);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(DaftarKeuangan.class));
    }

    public List<DaftarKeuangan> findByAmountGreaterThan(Double amount) {
        String sql = "SELECT * FROM daftar_keuangan WHERE amount > :amount";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("amount", amount);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(DaftarKeuangan.class));
    }

    public List<DaftarKeuangan> findByAmountLessThan(Double amount) {
        String sql = "SELECT * FROM daftar_keuangan WHERE amount < :amount";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("amount", amount);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(DaftarKeuangan.class));
    }

    public List<DaftarKeuangan> findByDateBetween(String startDate, String endDate) {
        String sql = "SELECT * FROM daftar_keuangan WHERE date BETWEEN :startDate AND :endDate";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(DaftarKeuangan.class));
    }
}
