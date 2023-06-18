package com.domain.controllers;

import java.util.List;
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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DaftarKeuangan daftarKeuangan) {
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

        DaftarKeuangan savedDaftarKeuangan = daftarKeuanganService.save(daftarKeuangan);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDaftarKeuangan);
    }

    @GetMapping
    public List<DaftarKeuangan> findAll() {
        return daftarKeuanganService.findAll();
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        daftarKeuanganService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }

    @GetMapping("/kategori/{kategoriId}")
    public List<DaftarKeuangan> findByKategoriId(@PathVariable Long kategoriId) {
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
