package com.domain.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daftar_keuangan")
public class DaftarKeuangan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

    private BigDecimal amount;

    private LocalDate date;

    @JsonProperty("kategoriId")
    private void setKategoriId(Long kategoriId) {
        this.kategori = new Kategori(kategoriId);
    }

    public DaftarKeuangan() {
    }

    public DaftarKeuangan(Kategori kategori, BigDecimal amount, LocalDate date) {
        this.kategori = kategori;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
