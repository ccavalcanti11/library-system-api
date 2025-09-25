package com.librarysystem.repository;

import com.librarysystem.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {

    Optional<Author> findByEmail(String email);

    List<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    List<Author> findByNationalityIgnoreCase(String nationality);

    @Query("{'$or': [" +
           "{'firstName': {'$regex': ?0, '$options': 'i'}}, " +
           "{'lastName': {'$regex': ?0, '$options': 'i'}}, " +
           "{'email': {'$regex': ?0, '$options': 'i'}}, " +
           "{'biography': {'$regex': ?0, '$options': 'i'}}, " +
           "{'nationality': {'$regex': ?0, '$options': 'i'}}, " +
           "{'genres': {'$regex': ?0, '$options': 'i'}}" +
           "]}")
    List<Author> searchAuthors(String keyword);

    @Query("{'$or': [" +
           "{'firstName': {'$regex': ?0, '$options': 'i'}}, " +
           "{'lastName': {'$regex': ?0, '$options': 'i'}}, " +
           "{'$expr': {'$regexMatch': {'input': {'$concat': ['$firstName', ' ', '$lastName']}, 'regex': ?0, 'options': 'i'}}}" +
           "]}")
    List<Author> searchByName(String name);
}
