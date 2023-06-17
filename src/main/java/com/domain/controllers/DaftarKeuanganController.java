package com.domain.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.domain.models.entities.DaftarKeuangan;
import com.domain.models.entities.Kategori;
import com.domain.services.DaftarKeuanganService;
import com.domain.services.KategoriService;

@RestController
@RequestMapping("/api/daftar-keuangan")
public class DaftarKeuanganController {
    private final DaftarKeuanganService daftarKeuanganService;
    private final KategoriService kategoriService;

    @Autowired
    public DaftarKeuanganController(DaftarKeuanganService daftarKeuanganService, KategoriService kategoriService) {
        this.daftarKeuanganService = daftarKeuanganService;
        this.kategoriService = kategoriService;
    }

    @PostMapping
    public DaftarKeuangan create(@RequestBody DaftarKeuangan daftarKeuangan) {
        // Memastikan bahwa kategori dengan kategoriId tersedia di tbl_kategori
        Long kategoriId = daftarKeuangan.getKategori().getId();
        Optional<Kategori> kategori = kategoriService.findById(kategoriId);
        if (kategori.isPresent()) {
            daftarKeuangan.setKategori(kategori.get()); // Set objek Kategori
            return daftarKeuanganService.save(daftarKeuangan);
        } else {
            throw new IllegalArgumentException("Kategori dengan ID " + kategoriId + " tidak ditemukan.");
        }
    }

    @GetMapping
    public List<DaftarKeuangan> findAll() {
        return daftarKeuanganService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DaftarKeuangan> findById(@PathVariable("id") Long id) {
        Optional<DaftarKeuangan> daftarKeuangan = daftarKeuanganService.findById(id);
        return daftarKeuangan.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public DaftarKeuangan update(@PathVariable("id") Long id, @RequestBody DaftarKeuangan daftarKeuangan) {
        DaftarKeuangan existingDaftarKeuangan = daftarKeuanganService.findById(id).orElse(null);
        if (existingDaftarKeuangan != null) {
            existingDaftarKeuangan.setKategori(daftarKeuangan.getKategori());
            existingDaftarKeuangan.setAmount(daftarKeuangan.getAmount());
            existingDaftarKeuangan.setDate(daftarKeuangan.getDate());
            return daftarKeuanganService.update(existingDaftarKeuangan);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        daftarKeuanganService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }

    @GetMapping("/kategori/{kategoriId}")
    public List<DaftarKeuangan> findByKategoriId(@PathVariable("kategoriId") Long kategoriId) {
        return daftarKeuanganService.findByKategoriId(kategoriId);
    }

    @GetMapping("/amount-greater-than/{amount}")
    public List<DaftarKeuangan> findByAmountGreaterThan(@PathVariable("amount") Double amount) {
        return daftarKeuanganService.findByAmountGreaterThan(amount);
    }

    @GetMapping("/amount-less-than/{amount}")
    public List<DaftarKeuangan> findByAmountLessThan(@PathVariable("amount") Double amount) {
        return daftarKeuanganService.findByAmountLessThan(amount);
    }

    @GetMapping("/date-between/{startDate}/{endDate}")
    public List<DaftarKeuangan> findByDateBetween(
            @PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        return daftarKeuanganService.findByDateBetween(startDate, endDate);
    }
}
