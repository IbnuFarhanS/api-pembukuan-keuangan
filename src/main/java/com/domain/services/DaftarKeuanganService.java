package com.domain.services;

import com.domain.models.entities.DaftarKeuangan;
import com.domain.models.entities.Kategori;
import com.domain.models.entities.Pengguna;
import com.domain.models.repos.DaftarKeuanganRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DaftarKeuanganService {

    private final DaftarKeuanganRepo daftarKeuanganRepo;
    private final KategoriService kategoriService;
    private final PenggunaService penggunaService;

    @Autowired
    public DaftarKeuanganService(DaftarKeuanganRepo daftarKeuanganRepo, KategoriService kategoriService, PenggunaService penggunaService) {
        this.daftarKeuanganRepo = daftarKeuanganRepo;
        this.kategoriService = kategoriService;
        this.penggunaService = penggunaService;
    }

    public DaftarKeuangan save(DaftarKeuangan daftarKeuangan) {
        Long kategoriId = daftarKeuangan.getKategori().getId();
        Optional<Kategori> kategori = kategoriService.findById(kategoriId);
        if (kategori.isPresent()) {
            daftarKeuangan.setKategori(kategori.get());
        } else {
            throw new IllegalArgumentException("Kategori dengan ID " + kategoriId + " tidak ditemukan.");
        }

        Long penggunaId = daftarKeuangan.getPengguna().getId();
        Optional<Pengguna> pengguna = penggunaService.findById(penggunaId);
        if (pengguna.isPresent()) {
            daftarKeuangan.setPengguna(pengguna.get());
        } else {
            throw new IllegalArgumentException("Pengguna dengan ID " + penggunaId + " tidak ditemukan.");
        }

        BigDecimal amount = daftarKeuangan.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount harus lebih besar dari 0.");
        }

        DaftarKeuangan savedDaftarKeuangan = daftarKeuanganRepo.save(daftarKeuangan);
        return savedDaftarKeuangan;
    }

    public Optional<DaftarKeuangan> findById(Long id) {
        Optional<DaftarKeuangan> daftarKeuangan = Optional.ofNullable(daftarKeuanganRepo.findById(id));
        if (!daftarKeuangan.isPresent()) {
            System.out.println("ID tidak ditemukan");
        }
        return daftarKeuangan;
    }

    public List<DaftarKeuangan> findAll() {
        return daftarKeuanganRepo.findAll();
    }

    public void deleteById(Long id) {
        daftarKeuanganRepo.delete(id);
    }

    public DaftarKeuangan update(DaftarKeuangan daftarKeuangan) {
        // Validasi Kategori
        Long kategoriId = daftarKeuangan.getKategori().getId();
        Kategori kategori = kategoriService.findById(kategoriId).orElse(null);
        if (kategori == null) {
            throw new IllegalArgumentException("Data kategori dengan ID " + kategoriId + " tidak ditemukan.");
        }
        daftarKeuangan.getKategori().setName(kategori.getName());

        // Validasi Pengguna
        Long penggunaId = daftarKeuangan.getPengguna().getId();
        Pengguna pengguna = penggunaService.findById(penggunaId).orElse(null);
        if (pengguna == null) {
            throw new IllegalArgumentException("Data pengguna dengan ID " + penggunaId + " tidak ditemukan.");
        }
        daftarKeuangan.getPengguna().setNamaPengguna(pengguna.getNamaPengguna());
        daftarKeuangan.getPengguna().setEmail(pengguna.getEmail());
        daftarKeuangan.getPengguna().setPassword(pengguna.getPassword());

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

    public List<DaftarKeuangan> findByKategoriId(Long kategoriId) {
        return daftarKeuanganRepo.findByKategoriId(kategoriId);
    }

    public List<DaftarKeuangan> findByPenggunaId(Long penggunaId) {
        return daftarKeuanganRepo.findByPenggunaId(penggunaId);
    }

    public List<DaftarKeuangan> findByAmountGreaterThan(Double amount) {
        return daftarKeuanganRepo.findByAmountGreaterThan(amount);
    }

    public List<DaftarKeuangan> findByAmountLessThan(Double amount) {
        return daftarKeuanganRepo.findByAmountLessThan(amount);
    }

    public List<DaftarKeuangan> findByDateBetween(String startDate, String endDate) {
        return daftarKeuanganRepo.findByDateBetween(startDate, endDate);
    }
}
