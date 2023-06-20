package com.domain.services;

import com.domain.models.entities.Customer;
import com.domain.models.entities.DaftarKeuangan;
import com.domain.models.entities.Kategori;
import com.domain.models.entities.Pengguna;
import com.domain.models.repos.DaftarKeuanganRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DaftarKeuanganService {

    private final DaftarKeuanganRepo daftarKeuanganRepo;
    private final PenggunaService penggunaService;
    private final CustomerService customerService;
    private final KategoriService kategoriService;

    @Autowired
    public DaftarKeuanganService(DaftarKeuanganRepo daftarKeuanganRepo, PenggunaService penggunaService, CustomerService customerService, KategoriService kategoriService) {
        this.daftarKeuanganRepo = daftarKeuanganRepo;
        this.penggunaService = penggunaService;
        this.customerService = customerService;
        this.kategoriService = kategoriService;
    }

    // ============================== FIND ALL ID ====================================
    public List<DaftarKeuangan> findAll() {
        return daftarKeuanganRepo.findAll();
    }

    // ============================== FIND BY ID ====================================
    public Optional<DaftarKeuangan> findById(Long id) {
        Optional<DaftarKeuangan> daftarKeuangan = Optional.ofNullable(daftarKeuanganRepo.findById(id));
        if (!daftarKeuangan.isPresent()) {
            System.out.println("ID tidak ditemukan");
        }
        return daftarKeuangan;
    }

    // ============================== SAVE ====================================
    public DaftarKeuangan save(DaftarKeuangan daftarKeuangan) {
        Long penggunaId = daftarKeuangan.getPengguna().getId();
        Optional<Pengguna> pengguna = penggunaService.findById(penggunaId);
        if (pengguna.isPresent()) {
            daftarKeuangan.setPengguna(pengguna.get());
        } else {
            throw new IllegalArgumentException("Pengguna dengan ID " + penggunaId + " tidak ditemukan.");
        }

        Long customerId = daftarKeuangan.getCustomer().getId();
        Optional<Customer> customer = customerService.findById(customerId);
        if (customer.isPresent()) {
            daftarKeuangan.setCustomer(customer.get());
        } else {
            throw new IllegalArgumentException("Customer dengan ID " + customerId + " tidak ditemukan.");
        }

        Long kategoriId = daftarKeuangan.getKategori().getId();
        Optional<Kategori> kategori = kategoriService.findById(kategoriId);
        if (kategori.isPresent()) {
            daftarKeuangan.setKategori(kategori.get());
        } else {
            throw new IllegalArgumentException("Kategori dengan ID " + kategoriId + " tidak ditemukan.");
        }

        BigDecimal amount = daftarKeuangan.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount harus lebih besar dari 0.");
        }

        DaftarKeuangan savedDaftarKeuangan = daftarKeuanganRepo.save(daftarKeuangan);
        return savedDaftarKeuangan;
    }

    // ============================== UPDATE ====================================
    public DaftarKeuangan update(DaftarKeuangan daftarKeuangan) {
        // Validasi Pengguna
        Long penggunaId = daftarKeuangan.getPengguna().getId();
        Pengguna pengguna = penggunaService.findById(penggunaId).orElse(null);
        if (pengguna == null) {
            throw new IllegalArgumentException("Data pengguna dengan ID " + penggunaId + " tidak ditemukan.");
        }
        daftarKeuangan.getPengguna().setNamaPengguna(pengguna.getNamaPengguna());
        daftarKeuangan.getPengguna().setEmail(pengguna.getEmail());
        daftarKeuangan.getPengguna().setPassword(pengguna.getPassword());

        // Validasi Customer
        Long customerId = daftarKeuangan.getCustomer().getId();
        Customer customer = customerService.findById(customerId).orElse(null);
        if (customer == null) {
            throw new IllegalArgumentException("Data customer dengan ID " + customerId + " tidak ditemukan.");
        }
        daftarKeuangan.getCustomer().setNamaCustomer(customer.getNamaCustomer());
        daftarKeuangan.getCustomer().setNomor(customer.getNomor());
        daftarKeuangan.getCustomer().setAlamat(customer.getAlamat());

        // Validasi Kategori
        Long kategoriId = daftarKeuangan.getKategori().getId();
        Kategori kategori = kategoriService.findById(kategoriId).orElse(null);
        if (kategori == null) {
            throw new IllegalArgumentException("Data kategori dengan ID " + kategoriId + " tidak ditemukan.");
        }
        daftarKeuangan.getKategori().setName(kategori.getName());

        // Validasi Amount
        BigDecimal amount = daftarKeuangan.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount harus lebih besar dari 0.");
        }

        // Melakukan pembaruan
        int rowsAffected = daftarKeuanganRepo.update(daftarKeuangan);
        if (rowsAffected <= 0) {
            throw new IllegalStateException("Gagal memperbarui daftar keuangan.");
        }

        return daftarKeuangan;
    }

    // ============================== DELETE ====================================
    public void deleteById(Long id) {
        daftarKeuanganRepo.delete(id);
    }

    // ============================== FIND BY ID Kategori ====================================
    public List<DaftarKeuangan> findByKategoriId(Long kategoriId) {
        return daftarKeuanganRepo.findByKategoriId(kategoriId);
    }

    // ============================== FIND BY ID Pengguna ====================================
    public List<DaftarKeuangan> findByPenggunaId(Long penggunaId) {
        return daftarKeuanganRepo.findByPenggunaId(penggunaId);
    }

    // ============================== FIND BY Amount Greater Than ====================================
    public List<DaftarKeuangan> findByAmountGreaterThan(Double amount) {
        return daftarKeuanganRepo.findByAmountGreaterThan(amount);
    }

    // ============================== FIND BY Amount Less Than ====================================
    public List<DaftarKeuangan> findByAmountLessThan(Double amount) {
        return daftarKeuanganRepo.findByAmountLessThan(amount);
    }

    // ============================== FIND BY Date Between ====================================
    public List<DaftarKeuangan> findByDateBetween(String startDate, String endDate) {
        return daftarKeuanganRepo.findByDateBetween(startDate, endDate);
    }

    // ============================== FIND ALL BY Amount Grouped ====================================
    public Map<BigDecimal, List<DaftarKeuangan>> findAllByAmountGrouped() {
        return daftarKeuanganRepo.findAllByAmountGrouped();
    }
}
