package com.librarysystem.service;

import com.librarysystem.model.Author;
import com.librarysystem.repository.AuthorRepository;
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
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Cacheable(value = "authors", key = "#id")
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Cacheable(value = "authors")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @CachePut(value = "authors", key = "#result.id")
    public Author save(Author author) {
        validateAuthor(author);
        return authorRepository.save(author);
    }

    @CachePut(value = "authors", key = "#id")
    public Author update(String id, Author updatedAuthor) {
        return authorRepository.findById(id)
            .map(author -> {
                updateAuthorFields(author, updatedAuthor);
                author.updateTimestamp();
                return authorRepository.save(author);
            })
            .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
    }

    @CacheEvict(value = "authors", key = "#id")
    public void deleteById(String id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    @Cacheable(value = "authors", key = "#email")
    public Optional<Author> findByEmail(String email) {
        return authorRepository.findByEmail(email);
    }

    public List<Author> findByName(String firstName, String lastName) {
        return authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
    }

    public List<Author> findByNationality(String nationality) {
        return authorRepository.findByNationalityIgnoreCase(nationality);
    }

    public List<Author> searchAuthors(String keyword) {
        return authorRepository.searchAuthors(keyword);
    }

    public List<Author> searchByName(String name) {
        return authorRepository.searchByName(name);
    }

    private void validateAuthor(Author author) {
        if (author.getEmail() != null && authorRepository.findByEmail(author.getEmail()).isPresent()) {
            throw new RuntimeException("Author with email " + author.getEmail() + " already exists");
        }
    }

    private void updateAuthorFields(Author existingAuthor, Author updatedAuthor) {
        if (updatedAuthor.getFirstName() != null) {
            existingAuthor.setFirstName(updatedAuthor.getFirstName());
        }
        if (updatedAuthor.getLastName() != null) {
            existingAuthor.setLastName(updatedAuthor.getLastName());
        }
        if (updatedAuthor.getEmail() != null) {
            existingAuthor.setEmail(updatedAuthor.getEmail());
        }
        if (updatedAuthor.getBiography() != null) {
            existingAuthor.setBiography(updatedAuthor.getBiography());
        }
        if (updatedAuthor.getNationality() != null) {
            existingAuthor.setNationality(updatedAuthor.getNationality());
        }
        if (updatedAuthor.getBirthDate() != null) {
            existingAuthor.setBirthDate(updatedAuthor.getBirthDate());
        }
        if (updatedAuthor.getWebsite() != null) {
            existingAuthor.setWebsite(updatedAuthor.getWebsite());
        }
        if (updatedAuthor.getGenres() != null) {
            existingAuthor.setGenres(updatedAuthor.getGenres());
        }
    }
}
