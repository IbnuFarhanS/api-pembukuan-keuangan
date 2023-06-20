package com.domain.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.models.entities.Customer;
import com.domain.models.repos.CustomerRepo;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo){
        this.customerRepo = customerRepo;
    }

    // ============================== FIND ALL ID ====================================
    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    // ============================== FIND BY ID ====================================
    public Optional<Customer> findById(Long id) {
        return customerRepo.findById(id);
    }

    // ============================== FIND BY NAMA CUSTOMER ====================================
    public List<Customer> findByNamaCustomerContains(String namaCustomer) {
        List<Customer> customers = customerRepo.findByNamaCustomerContains(namaCustomer);
        if (customers.isEmpty()) {
            throw new IllegalArgumentException("Customer '" + namaCustomer + "'  tidak ditemukan.");
        }
        return customers;
    }

    // ============================== SAVE ====================================
    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    // ============================== UPDATE ====================================
    public Customer update(Customer customer) {
        Optional<Customer> existingCustomerOpt = customerRepo.findById(customer.getId());
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setNamaCustomer(customer.getNamaCustomer());
            existingCustomer.setNomor(customer.getNomor());
            existingCustomer.setAlamat(customer.getAlamat());

            return customerRepo.update(existingCustomer);
        } else {
            throw new IllegalArgumentException("Customer dengan ID '" + customer.getId() + "' tidak ditemukan.");
        }
    }

    // ============================== DELETE ====================================
    public void deleteById(Long id) {
        customerRepo.delete(id);
    }
}
