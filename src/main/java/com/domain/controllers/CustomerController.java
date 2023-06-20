package com.domain.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.domain.models.entities.Customer;
import com.domain.services.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    // ============================== FIND ALL ID ====================================
    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    // ============================== FIND BY ID ====================================
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Data dengan ID " + id + " tidak ditemukan.");
        }
    }

    // ============================== FIND BY NAMA CUSTOMER ====================================
    @GetMapping("/findByNamaCustomer/{namaCustomer}")
    public ResponseEntity<?> findByNamaCustomer(@PathVariable String namaCustomer) {
        try {
            return ResponseEntity.ok(customerService.findByNamaCustomerContains(namaCustomer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ============================== SAVE ====================================
    @PostMapping
    public ResponseEntity<Customer> save(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.save(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    // ============================== UPDATE ====================================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Customer customer) {
        Customer existingCustomer = customerService.findById(id).orElse(null);
        if (existingCustomer != null) {
            existingCustomer.setNamaCustomer(customer.getNamaCustomer());
            existingCustomer.setNomor(customer.getNomor());
            existingCustomer.setAlamat(customer.getAlamat());

            Customer updatedCustomer = customerService.update(existingCustomer);
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Data dengan ID " + id + " tidak ditemukan.");
        }
    }

    // ============================== DELETE ====================================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("Data dengan ID " + id + " berhasil dihapus.");
    }
}
