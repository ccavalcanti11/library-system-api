package com.librarysystem.service;

import com.librarysystem.model.Borrower;
import com.librarysystem.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Autowired
    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    @Cacheable(value = "borrowers", key = "#id")
    public Optional<Borrower> findById(String id) {
        return borrowerRepository.findById(id);
    }

    @Cacheable(value = "borrowers")
    public List<Borrower> findAll() {
        return borrowerRepository.findAll();
    }

    @CachePut(value = "borrowers", key = "#result.id")
    public Borrower save(Borrower borrower) {
        validateBorrower(borrower);
        return borrowerRepository.save(borrower);
    }

    @CachePut(value = "borrowers", key = "#id")
    public Borrower update(String id, Borrower updatedBorrower) {
        return borrowerRepository.findById(id)
            .map(borrower -> {
                updateBorrowerFields(borrower, updatedBorrower);
                borrower.updateTimestamp();
                return borrowerRepository.save(borrower);
            })
            .orElseThrow(() -> new RuntimeException("Borrower not found with id: " + id));
    }

    @CacheEvict(value = "borrowers", key = "#id")
    public void deleteById(String id) {
        if (!borrowerRepository.existsById(id)) {
            throw new RuntimeException("Borrower not found with id: " + id);
        }
        borrowerRepository.deleteById(id);
    }

    @Cacheable(value = "borrowers", key = "#email")
    public Optional<Borrower> findByEmail(String email) {
        return borrowerRepository.findByEmail(email);
    }

    public List<Borrower> findActiveMembers() {
        return borrowerRepository.findByActive(true);
    }

    public List<Borrower> findInactiveMembers() {
        return borrowerRepository.findByActive(false);
    }

    public List<Borrower> searchBorrowers(String keyword) {
        return borrowerRepository.searchBorrowers(keyword);
    }

    public List<Borrower> searchByName(String name) {
        return borrowerRepository.searchByName(name);
    }

    public List<Borrower> findByCity(String city) {
        return borrowerRepository.findByCityIgnoreCase(city);
    }

    @CachePut(value = "borrowers", key = "#id")
    public Borrower deactivateMember(String id) {
        return borrowerRepository.findById(id)
            .map(borrower -> {
                borrower.setActive(false);
                borrower.updateTimestamp();
                return borrowerRepository.save(borrower);
            })
            .orElseThrow(() -> new RuntimeException("Borrower not found with id: " + id));
    }

    @CachePut(value = "borrowers", key = "#id")
    public Borrower reactivateMember(String id) {
        return borrowerRepository.findById(id)
            .map(borrower -> {
                borrower.setActive(true);
                borrower.updateTimestamp();
                return borrowerRepository.save(borrower);
            })
            .orElseThrow(() -> new RuntimeException("Borrower not found with id: " + id));
    }

    private void validateBorrower(Borrower borrower) {
        if (borrowerRepository.findByEmail(borrower.getEmail()).isPresent()) {
            throw new RuntimeException("Borrower with email " + borrower.getEmail() + " already exists");
        }
    }

    private void updateBorrowerFields(Borrower existingBorrower, Borrower updatedBorrower) {
        if (updatedBorrower.getFirstName() != null) {
            existingBorrower.setFirstName(updatedBorrower.getFirstName());
        }
        if (updatedBorrower.getLastName() != null) {
            existingBorrower.setLastName(updatedBorrower.getLastName());
        }
        if (updatedBorrower.getEmail() != null) {
            existingBorrower.setEmail(updatedBorrower.getEmail());
        }
        if (updatedBorrower.getPhoneNumber() != null) {
            existingBorrower.setPhoneNumber(updatedBorrower.getPhoneNumber());
        }
        if (updatedBorrower.getAddress() != null) {
            existingBorrower.setAddress(updatedBorrower.getAddress());
        }
        if (updatedBorrower.getCity() != null) {
            existingBorrower.setCity(updatedBorrower.getCity());
        }
        if (updatedBorrower.getPostalCode() != null) {
            existingBorrower.setPostalCode(updatedBorrower.getPostalCode());
        }
        if (updatedBorrower.getCountry() != null) {
            existingBorrower.setCountry(updatedBorrower.getCountry());
        }
    }
}
