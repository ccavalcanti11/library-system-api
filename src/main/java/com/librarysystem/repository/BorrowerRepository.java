package com.librarysystem.repository;

import com.librarysystem.model.Borrower;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowerRepository extends MongoRepository<Borrower, String> {

    Optional<Borrower> findByEmail(String email);

    List<Borrower> findByActive(boolean active);

    List<Borrower> findByCityIgnoreCase(String city);

    List<Borrower> findByCountryIgnoreCase(String country);

    @Query("{'$or': [" +
           "{'firstName': {$regex: ?0, $options: 'i'}}, " +
           "{'lastName': {$regex: ?0, $options: 'i'}}" +
           "]}")
    List<Borrower> searchByName(String name);

    @Query("{'$or': [" +
           "{'firstName': {$regex: ?0, $options: 'i'}}, " +
           "{'lastName': {$regex: ?0, $options: 'i'}}, " +
           "{'email': {$regex: ?0, $options: 'i'}}, " +
           "{'city': {$regex: ?0, $options: 'i'}}" +
           "]}")
    List<Borrower> searchBorrowers(String keyword);
}
