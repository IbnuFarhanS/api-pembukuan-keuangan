package com.domain.models.repos;

import com.domain.models.entities.Customer;
import com.domain.models.entities.DaftarKeuangan;
import com.domain.models.entities.Kategori;
import com.domain.models.entities.Pengguna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DaftarKeuanganRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DaftarKeuanganRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================== FIND ALL ID ====================================
    public List<DaftarKeuangan> findAll() {
        String sql = "SELECT dk.id, dk.amount, dk.date, p.id AS pengguna_id, p.nama_pengguna, p.username, p.email, p.password, c.id AS customer_id, c.nama_customer, c.nomor, c.alamat, k.id AS kategori_id, k.name AS kategori_name " +
                    "FROM daftar_keuangan dk " +
                    "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                    "JOIN pengguna p ON dk.pengguna_id = p.id " +
                    "JOIN customer c ON dk.customer_id = c.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Pengguna pengguna = new Pengguna();
            pengguna.setId(rs.getLong("pengguna_id"));
            pengguna.setNamaPengguna(rs.getString("nama_pengguna"));
            pengguna.setUsername(rs.getString("username"));
            pengguna.setEmail(rs.getString("email"));
            pengguna.setPassword(rs.getString("password"));
            daftarKeuangan.setPengguna(pengguna);

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setNamaCustomer(rs.getString("nama_customer"));
            customer.setNomor(rs.getString("nomor"));
            customer.setAlamat(rs.getString("alamat"));
            daftarKeuangan.setCustomer(customer);

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
    }

    // ============================== FIND BY ID ====================================
    public DaftarKeuangan findById(Long id) {
        String sql = "SELECT dk.id, dk.amount, dk.date, p.id AS pengguna_id, p.nama_pengguna, p.username, p.email, p.password, c.id AS customer_id, c.nama_customer, c.nomor, c.alamat, k.id AS kategori_id, k.name AS kategori_name " +
                    "FROM daftar_keuangan dk " +
                    "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                    "JOIN pengguna p ON dk.pengguna_id = p.id " +
                    "JOIN customer c ON dk.customer_id = c.id " +
                    "WHERE dk.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        List<DaftarKeuangan> result = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Pengguna pengguna = new Pengguna();
            pengguna.setId(rs.getLong("pengguna_id"));
            pengguna.setNamaPengguna(rs.getString("nama_pengguna"));
            pengguna.setUsername(rs.getString("username"));
            pengguna.setEmail(rs.getString("email"));
            pengguna.setPassword(rs.getString("password"));
            daftarKeuangan.setPengguna(pengguna);

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setNamaCustomer(rs.getString("nama_customer"));
            customer.setNomor(rs.getString("nomor"));
            customer.setAlamat(rs.getString("alamat"));
            daftarKeuangan.setCustomer(customer);

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
        return result.isEmpty() ? null : result.get(0);
    }

    // ============================== SAVE ====================================
    public DaftarKeuangan save(DaftarKeuangan daftarKeuangan) {
        String sql = "INSERT INTO daftar_keuangan (pengguna_id, customer_id, kategori_id, amount, date) VALUES (:penggunaId, :customerId, :kategoriId, :amount, :date)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("penggunaId", daftarKeuangan.getPengguna().getId())
                .addValue("customerId", daftarKeuangan.getCustomer().getId())
                .addValue("kategoriId", daftarKeuangan.getKategori().getId())
                .addValue("amount", daftarKeuangan.getAmount())
                .addValue("date", daftarKeuangan.getDate());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = keyHolder.getKey().longValue();
        daftarKeuangan.setId(generatedId);

        return daftarKeuangan;
    }

    // ============================== UPDATE ====================================
    public int update(DaftarKeuangan daftarKeuangan) {
        String sql = "UPDATE daftar_keuangan SET pengguna_id = :penggunaId, customer_id = :customerId, kategori_id = :kategoriId, amount = :amount, date = :date WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("penggunaId", daftarKeuangan.getPengguna().getId())
                .addValue("customerId", daftarKeuangan.getCustomer().getId())
                .addValue("kategoriId", daftarKeuangan.getKategori().getId())
                .addValue("amount", daftarKeuangan.getAmount())
                .addValue("date", daftarKeuangan.getDate())
                .addValue("id", daftarKeuangan.getId());
        return jdbcTemplate.update(sql, params);
    }

    // ============================== DELETE ====================================
    public void delete(Long id) {
        String sql = "DELETE FROM daftar_keuangan WHERE id = :id";
        jdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

    // ============================== FIND BY ID Kategori ====================================
    public List<DaftarKeuangan> findByKategoriId(Long kategoriId) {
        String sql = "SELECT dk.id, dk.amount, dk.date, p.id AS pengguna_id, p.nama_pengguna, p.username, p.email, p.password, c.id AS customer_id, c.nama_customer, c.nomor, c.alamat, k.id AS kategori_id, k.name AS kategori_name " +
                    "FROM daftar_keuangan dk " +
                    "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                    "JOIN pengguna p ON dk.pengguna_id = p.id " +
                    "JOIN customer c ON dk.customer_id = c.id " +
                    "WHERE dk.kategori_id = :kategoriId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("kategoriId", kategoriId);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Pengguna pengguna = new Pengguna();
            pengguna.setId(rs.getLong("pengguna_id"));
            pengguna.setNamaPengguna(rs.getString("nama_pengguna"));
            pengguna.setUsername(rs.getString("username"));
            pengguna.setEmail(rs.getString("email"));
            pengguna.setPassword(rs.getString("password"));
            daftarKeuangan.setPengguna(pengguna);

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setNamaCustomer(rs.getString("nama_customer"));
            customer.setNomor(rs.getString("nomor"));
            customer.setAlamat(rs.getString("alamat"));
            daftarKeuangan.setCustomer(customer);

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
    }

    // ============================== FIND BY ID Pengguna ====================================
    public List<DaftarKeuangan> findByPenggunaId(Long penggunaId) {
        String sql = "SELECT dk.id, dk.amount, dk.date, p.id AS pengguna_id, p.nama_pengguna, p.username, p.email, p.password, c.id AS customer_id, c.nama_customer, c.nomor, c.alamat, k.id AS kategori_id, k.name AS kategori_name " +
                    "FROM daftar_keuangan dk " +
                    "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                    "JOIN pengguna p ON dk.pengguna_id = p.id " +
                    "JOIN customer c ON dk.customer_id = c.id " +
                    "WHERE dk.pengguna_id = :penggunaId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("penggunaId", penggunaId);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Pengguna pengguna = new Pengguna();
            pengguna.setId(rs.getLong("pengguna_id"));
            pengguna.setNamaPengguna(rs.getString("nama_pengguna"));
            pengguna.setUsername(rs.getString("username"));
            pengguna.setEmail(rs.getString("email"));
            pengguna.setPassword(rs.getString("password"));
            daftarKeuangan.setPengguna(pengguna);

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setNamaCustomer(rs.getString("nama_customer"));
            customer.setNomor(rs.getString("nomor"));
            customer.setAlamat(rs.getString("alamat"));
            daftarKeuangan.setCustomer(customer);

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
    }

    // ============================== FIND BY Amount Greater Than ====================================
    public List<DaftarKeuangan> findByAmountGreaterThan(Double amount) {
        String sql = "SELECT dk.id, dk.amount, dk.date, p.id AS pengguna_id, p.nama_pengguna, p.username, p.email, p.password, c.id AS customer_id, c.nama_customer, c.nomor, c.alamat, k.id AS kategori_id, k.name AS kategori_name " +
                    "FROM daftar_keuangan dk " +
                    "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                    "JOIN pengguna p ON dk.pengguna_id = p.id " +
                    "JOIN customer c ON dk.customer_id = c.id " +
                    "WHERE dk.amount > :amount";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("amount", amount);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Pengguna pengguna = new Pengguna();
            pengguna.setId(rs.getLong("pengguna_id"));
            pengguna.setNamaPengguna(rs.getString("nama_pengguna"));
            pengguna.setUsername(rs.getString("username"));
            pengguna.setEmail(rs.getString("email"));
            pengguna.setPassword(rs.getString("password"));
            daftarKeuangan.setPengguna(pengguna);

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setNamaCustomer(rs.getString("nama_customer"));
            customer.setNomor(rs.getString("nomor"));
            customer.setAlamat(rs.getString("alamat"));
            daftarKeuangan.setCustomer(customer);

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
    }

    // ============================== FIND BY Amount Less Then ====================================
    public List<DaftarKeuangan> findByAmountLessThan(Double amount) {
        String sql = "SELECT dk.id, dk.amount, dk.date, p.id AS pengguna_id, p.nama_pengguna, p.username, p.email, p.password, c.id AS customer_id, c.nama_customer, c.nomor, c.alamat, k.id AS kategori_id, k.name AS kategori_name " +
                    "FROM daftar_keuangan dk " +
                    "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                    "JOIN pengguna p ON dk.pengguna_id = p.id " +
                    "JOIN customer c ON dk.customer_id = c.id " +
                    "WHERE dk.amount < :amount";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("amount", amount);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Pengguna pengguna = new Pengguna();
            pengguna.setId(rs.getLong("pengguna_id"));
            pengguna.setNamaPengguna(rs.getString("nama_pengguna"));
            pengguna.setUsername(rs.getString("username"));
            pengguna.setEmail(rs.getString("email"));
            pengguna.setPassword(rs.getString("password"));
            daftarKeuangan.setPengguna(pengguna);

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setNamaCustomer(rs.getString("nama_customer"));
            customer.setNomor(rs.getString("nomor"));
            customer.setAlamat(rs.getString("alamat"));
            daftarKeuangan.setCustomer(customer);

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
    }

    // ============================== FIND BY DATE BETWEEN ====================================
    public List<DaftarKeuangan> findByDateBetween(String startDate, String endDate) {
        String sql = "SELECT dk.id, dk.amount, dk.date, p.id AS pengguna_id, p.nama_pengguna, p.username, p.email, p.password, c.id AS customer_id, c.nama_customer, c.nomor, c.alamat, k.id AS kategori_id, k.name AS kategori_name " +
                    "FROM daftar_keuangan dk " +
                    "JOIN tbl_kategori k ON dk.kategori_id = k.id " +
                    "JOIN pengguna p ON dk.pengguna_id = p.id " +
                    "JOIN customer c ON dk.customer_id = c.id " +
                    "WHERE dk.date BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            DaftarKeuangan daftarKeuangan = new DaftarKeuangan();
            daftarKeuangan.setId(rs.getLong("id"));
            daftarKeuangan.setAmount(rs.getBigDecimal("amount"));
            daftarKeuangan.setDate(rs.getDate("date").toLocalDate());

            Pengguna pengguna = new Pengguna();
            pengguna.setId(rs.getLong("pengguna_id"));
            pengguna.setNamaPengguna(rs.getString("nama_pengguna"));
            pengguna.setUsername(rs.getString("username"));
            pengguna.setEmail(rs.getString("email"));
            pengguna.setPassword(rs.getString("password"));
            daftarKeuangan.setPengguna(pengguna);

            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setNamaCustomer(rs.getString("nama_customer"));
            customer.setNomor(rs.getString("nomor"));
            customer.setAlamat(rs.getString("alamat"));
            daftarKeuangan.setCustomer(customer);

            Kategori kategori = new Kategori();
            kategori.setId(rs.getLong("kategori_id"));
            kategori.setName(rs.getString("kategori_name"));
            daftarKeuangan.setKategori(kategori);

            return daftarKeuangan;
        });
    }

    // Pengguna Java Stream collector untuk mengelompokan amount dari kecil ke besar
    public Map<BigDecimal, List<DaftarKeuangan>> findAllByAmountGrouped() {
        List<DaftarKeuangan> daftarKeuangans = findAll();

        return daftarKeuangans.stream()
                .collect(Collectors.groupingBy(DaftarKeuangan::getAmount))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
