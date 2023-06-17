package com.domain.services;

import com.domain.models.entities.DaftarKeuangan;
import com.domain.models.entities.Kategori;
import com.domain.models.repos.DaftarKeuanganRepo;
import com.domain.models.repos.KategoriRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DaftarKeuanganService {

    private final DaftarKeuanganRepo daftarKeuanganRepo;
    private final KategoriService kategoriService;

    @Autowired
    public DaftarKeuanganService(DaftarKeuanganRepo daftarKeuanganRepo, KategoriService kategoriService) {
        this.daftarKeuanganRepo = daftarKeuanganRepo;
        this.kategoriService = kategoriService;
    }

    public DaftarKeuangan save(DaftarKeuangan daftarKeuangan) {
        // Memastikan bahwa kategori dengan kategoriId tersedia di tbl_kategori
        Long kategoriId = daftarKeuangan.getKategori().getId();
        Optional<Kategori> kategori = kategoriService.findById(kategoriId);
        if (kategori.isPresent()) {
            daftarKeuangan.setKategori(kategori.get());
            return daftarKeuanganRepo.save(daftarKeuangan);
        } else {
            // Kategori tidak ditemukan, Anda dapat mengambil tindakan yang sesuai (misalnya, lempar Exception)
            throw new IllegalArgumentException("Kategori dengan ID " + kategoriId + " tidak ditemukan.");
        }
    }

    public Optional<DaftarKeuangan> findById(Long id) {
        return Optional.ofNullable(daftarKeuanganRepo.findById(id));
    }

    public List<DaftarKeuangan> findAll() {
        return daftarKeuanganRepo.findAll();
    }

    public void deleteById(Long id) {
        daftarKeuanganRepo.delete(id);
    }

    public DaftarKeuangan update(DaftarKeuangan daftarKeuangan) {
        int rowsAffected = daftarKeuanganRepo.update(daftarKeuangan);
        if (rowsAffected > 0) {
            return daftarKeuangan;
        } else {
            return null;
        }
    }

    public List<DaftarKeuangan> findByKategoriId(Long kategoriId) {
        return daftarKeuanganRepo.findByKategoriId(kategoriId);
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
