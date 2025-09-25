package com.librarysystem.repository;

import com.librarysystem.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorId(String authorId);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByPublicationYear(Integer publicationYear);

    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("{'tags': {$in: ?0}}")
    List<Book> findByTagsIn(List<String> tags);

    @Query("{'availableCopies': {$gt: 0}}")
    List<Book> findAvailableBooks();

    List<Book> findByAvailableCopiesGreaterThan(Integer availableCopies);

    @Query("{'$or': [" +
           "{'title': {$regex: ?0, $options: 'i'}}, " +
           "{'genre': {$regex: ?0, $options: 'i'}}, " +
           "{'publisher': {$regex: ?0, $options: 'i'}}" +
           "]}")
    List<Book> searchBooks(String keyword);

    @Query("{'$or': [{'title': {'$regex': ?0, '$options': 'i'}}, {'genre': {'$regex': ?0, '$options': 'i'}}, {'isbn': {'$regex': ?0, '$options': 'i'}}]}")
    List<Book> findByKeyword(String keyword);
}
