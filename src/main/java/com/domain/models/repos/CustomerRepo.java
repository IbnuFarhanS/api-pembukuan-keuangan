package com.domain.models.repos;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.domain.models.entities.Customer;

@Repository
public class CustomerRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerRepo(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================== FIND ALL ID ====================================
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customer";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customer.class));
    }

    // ============================== FIND BY ID ====================================
    public Optional<Customer> findById(Long id) {
        String sql = "SELECT * FROM customer WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Customer customer = jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Customer.class));
            return Optional.ofNullable(customer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // ============================== FIND BY NAMA CUSTOMER ====================================
    public List<Customer> findByNamaCustomerContains(String namaCustomer) {
        String sql = "SELECT * FROM customer WHERE nama_customer LIKE :namaCustomer";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("namaCustomer", "%" + namaCustomer + "%");

        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Customer.class));
    }

    // ============================== SAVE ====================================
    public Customer save(Customer customer) {
        String sql = "INSERT INTO customer (nama_customer, nomor, alamat) VALUES (:namaCustomer, :nomor, :alamat)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("namaCustomer", customer.getNamaCustomer())
                .addValue("nomor", customer.getNomor())
                .addValue("alamat", customer.getAlamat());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        customer.setId(generatedId);

        return customer;
    }

    // ============================== UPDATE ====================================
    public Customer update(Customer customer) {
        String sql = "UPDATE customer SET nama_customer = :namaCustomer, nomor = :nomor, alamat = :alamat WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("namaCustomer", customer.getNamaCustomer())
                .addValue("nomor", customer.getNomor())
                .addValue("alamat", customer.getAlamat())
                .addValue("id", customer.getId());

        jdbcTemplate.update(sql, params);

        return customer;
    }

    // ============================== DELETE ====================================
    public void delete(Long id) {
        String sql = "DELETE FROM customer WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, params);
    }
}
