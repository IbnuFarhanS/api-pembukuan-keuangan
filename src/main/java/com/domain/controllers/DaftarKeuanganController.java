package com.domain.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.domain.models.entities.DaftarKeuangan;
import com.domain.models.entities.Kategori;
import com.domain.models.entities.Pengguna;
import com.domain.services.DaftarKeuanganService;
import com.domain.services.KategoriService;
import com.domain.services.PenggunaService;

@RestController
@RequestMapping("/api/daftar-keuangan")
public class DaftarKeuanganController {
    private final DaftarKeuanganService daftarKeuanganService;
    private final KategoriService kategoriService;
    private final PenggunaService penggunaService;

    @Autowired
    public DaftarKeuanganController(DaftarKeuanganService daftarKeuanganService, KategoriService kategoriService, PenggunaService penggunaService) {
        this.daftarKeuanganService = daftarKeuanganService;
        this.kategoriService = kategoriService;
        this.penggunaService = penggunaService;
    }

    // ============================== FIND ALL ID ====================================
    @GetMapping
    public List<DaftarKeuangan> findAll() {
        return daftarKeuanganService.findAll();
    }

    // ============================== FIND BY ID ====================================
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<DaftarKeuangan> daftarKeuangan = daftarKeuanganService.findById(id);
        if (daftarKeuangan.isPresent()) {
            return ResponseEntity.ok(daftarKeuangan.get());
        } else {
            String message = "Data dengan ID " + id + " tidak ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    // ============================== SAVE ====================================
    @PostMapping
    public ResponseEntity<?> save(@RequestBody DaftarKeuangan daftarKeuangan) {
        // Memastikan bahwa kategori dengan kategoriId tersedia di tbl_kategori
        Long kategoriId = daftarKeuangan.getKategori().getId();
        Optional<Kategori> kategori = kategoriService.findById(kategoriId);
        if (kategori.isPresent()) {
            daftarKeuangan.setKategori(kategori.get()); // Set objek Kategori
        } else {
            String errorMessage = "Data kategori ID " + kategoriId + " tidak ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        // Memastikan bahwa pengguna dengan penggunaId tersedia di tabel pengguna
        Long penggunaId = daftarKeuangan.getPengguna().getId();
        Optional<Pengguna> pengguna = penggunaService.findById(penggunaId);
        if (pengguna.isPresent()) {
            daftarKeuangan.setPengguna(pengguna.get()); // Set objek Pengguna
        } else {
            String errorMessage = "Data pengguna ID " + penggunaId + " tidak ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        BigDecimal amount = daftarKeuangan.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            String errorMessage = "Amount harus lebih besar dari 0.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        DaftarKeuangan savedDaftarKeuangan = daftarKeuanganService.save(daftarKeuangan);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDaftarKeuangan);
    }

    // ============================== UPDATE ====================================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody DaftarKeuangan daftarKeuangan) {
        DaftarKeuangan existingDaftarKeuangan = daftarKeuanganService.findById(id).orElse(null);
        if (existingDaftarKeuangan != null) {
            Kategori kategori = daftarKeuangan.getKategori();
            if (kategori != null) {
                Kategori existingKategori = existingDaftarKeuangan.getKategori();
                existingKategori.setId(kategori.getId());
                existingKategori.setName(kategori.getName());
            }
            Pengguna pengguna = daftarKeuangan.getPengguna();
            if (pengguna != null) {
                Pengguna existingPengguna = existingDaftarKeuangan.getPengguna();
                existingPengguna.setId(pengguna.getId());
                existingPengguna.setNamaPengguna(pengguna.getNamaPengguna());
                existingPengguna.setEmail(pengguna.getEmail());
                existingPengguna.setPassword(pengguna.getPassword());
            }

            BigDecimal amount = daftarKeuangan.getAmount();
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                String errorMessage = "Amount harus lebih besar dari 0.";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
            existingDaftarKeuangan.setAmount(daftarKeuangan.getAmount());

            existingDaftarKeuangan.setDate(daftarKeuangan.getDate());

            try {
                DaftarKeuangan updatedDaftarKeuangan = daftarKeuanganService.update(existingDaftarKeuangan);
                return ResponseEntity.ok(updatedDaftarKeuangan);
            } catch (IllegalArgumentException e) {
                String errorMessage = e.getMessage();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            } catch (IllegalStateException e) {
                String errorMessage = e.getMessage();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            }
        } else {
            String errorMessage = "Data dengan ID " + id + " tidak ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    // ============================== DELETE ====================================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        daftarKeuanganService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }

    // ============================== FIND BY ID Kategori ====================================
    @GetMapping("/kategori/{kategoriId}")
    public ResponseEntity<?> findByKategoriId(@PathVariable Long kategoriId) {
        List<DaftarKeuangan> daftarKeuanganList = daftarKeuanganService.findByKategoriId(kategoriId);
        if (daftarKeuanganList.isEmpty()) {
            String errorMessage = "Tidak ada daftar keuangan dengan ID kategori " + kategoriId + " yang ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
        return ResponseEntity.ok(daftarKeuanganList);
    }

    // ============================== FIND BY ID Pengguna ====================================
    @GetMapping("/pengguna/{penggunaId}")
    public ResponseEntity<?> findByPenggunaId(@PathVariable Long penggunaId) {
        List<DaftarKeuangan> daftarKeuanganList = daftarKeuanganService.findByPenggunaId(penggunaId);
        if (daftarKeuanganList.isEmpty()) {
            String errorMessage = "Tidak ada daftar keuangan dengan ID pengguna " + penggunaId + " yang ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
        return ResponseEntity.ok(daftarKeuanganList);
    }

    // ============================== FIND BY Amount Greater Than ====================================
    @GetMapping("/amount-greater-than/{amount}")
    public ResponseEntity<?> findByAmountGreaterThan(@PathVariable("amount") Double amount) {
        List<DaftarKeuangan> daftarKeuanganList = daftarKeuanganService.findByAmountGreaterThan(amount);

        if (daftarKeuanganList.isEmpty()) {
            String errorMessage = "Tidak ada daftar keuangan dengan amount lebih besar dari " + amount + " yang ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        return ResponseEntity.ok(daftarKeuanganList);
    }

    // ============================== FIND BY Amount Less Than ====================================
    @GetMapping("/amount-less-than/{amount}")
    public ResponseEntity<?> findByAmountLessThan(@PathVariable("amount") Double amount) {
        List<DaftarKeuangan> daftarKeuanganList = daftarKeuanganService.findByAmountLessThan(amount);

        if (daftarKeuanganList.isEmpty()) {
            String errorMessage = "Tidak ada daftar keuangan dengan amount lebih kecil dari " + amount + " yang ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        return ResponseEntity.ok(daftarKeuanganList);
    }

    // ============================== FIND BY Date Between ====================================
    @GetMapping("/date-between/{startDate}/{endDate}")
    public ResponseEntity<?> findByDateBetween(
            @PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {
        List<DaftarKeuangan> daftarKeuanganList = daftarKeuanganService.findByDateBetween(startDate, endDate);

        if (daftarKeuanganList.isEmpty()) {
            String errorMessage = "Tidak ada daftar keuangan antara tanggal " + startDate + " dan " + endDate + " yang ditemukan.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        return ResponseEntity.ok(daftarKeuanganList);
    }

    // ============================== FIND ALL BY Amount Grouped ====================================
    @GetMapping("/grouped-by-amount")
    public ResponseEntity<Map<BigDecimal, List<DaftarKeuangan>>> getAllGroupedByAmount() {
        Map<BigDecimal, List<DaftarKeuangan>> groupedByAmount = daftarKeuanganService.findAllByAmountGrouped();
        return ResponseEntity.ok().body(groupedByAmount);
    }
}
