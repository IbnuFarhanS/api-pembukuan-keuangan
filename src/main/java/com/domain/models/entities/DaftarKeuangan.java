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
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

    private BigDecimal amount;

    private LocalDate date;

    @JsonProperty("penggunaId")
    private void setPenggunaId(Long penggunaId) {
        this.pengguna = new Pengguna(penggunaId);
    }

    @JsonProperty("customerId")
    private void setCustomerId(Long customerId) {
        this.customer = new Customer(customerId);
    }

    @JsonProperty("kategoriId")
    private void setKategoriId(Long kategoriId) {
        this.kategori = new Kategori(kategoriId);
    }

    public DaftarKeuangan() {
    }

    public DaftarKeuangan(Pengguna pengguna, Customer customer, Kategori kategori, BigDecimal amount, LocalDate date) {
        this.pengguna = pengguna;
        this.customer = customer;
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

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
