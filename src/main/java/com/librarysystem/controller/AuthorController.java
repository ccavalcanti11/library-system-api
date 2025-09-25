package com.librarysystem.controller;

import com.librarysystem.model.Author;
import com.librarysystem.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Author management operations")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieve a specific author by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Author found"),
        @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Author> getAuthorById(
            @Parameter(description = "Author ID", required = true) @PathVariable String id) {
        return authorService.findById(id)
                .map(author -> ResponseEntity.ok(author))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new author", description = "Add a new author to the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Author created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid author data")
    })
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        try {
            Author savedAuthor = authorService.save(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author", description = "Update an existing author")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Author updated successfully"),
        @ApiResponse(responseCode = "404", description = "Author not found"),
        @ApiResponse(responseCode = "400", description = "Invalid author data")
    })
    public ResponseEntity<Author> updateAuthor(
            @Parameter(description = "Author ID", required = true) @PathVariable String id,
            @Valid @RequestBody Author author) {
        try {
            Author updatedAuthor = authorService.update(id, author);
            return ResponseEntity.ok(updatedAuthor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Remove an author from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "Author ID", required = true) @PathVariable String id) {
        try {
            authorService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get author by email", description = "Retrieve an author by their email address")
    public ResponseEntity<Author> getAuthorByEmail(
            @Parameter(description = "Author email", required = true) @PathVariable String email) {
        return authorService.findByEmail(email)
                .map(author -> ResponseEntity.ok(author))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search authors", description = "Search authors by keyword in name, biography, or nationality")
    public ResponseEntity<List<Author>> searchAuthors(
            @Parameter(description = "Search keyword", required = true) @RequestParam String keyword) {
        List<Author> authors = authorService.searchAuthors(keyword);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/search/name")
    @Operation(summary = "Search authors by name", description = "Search authors by first or last name")
    public ResponseEntity<List<Author>> searchAuthorsByName(
            @Parameter(description = "Name to search for", required = true) @RequestParam String name) {
        List<Author> authors = authorService.searchByName(name);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/nationality/{nationality}")
    @Operation(summary = "Get authors by nationality", description = "Retrieve all authors from a specific nationality")
    public ResponseEntity<List<Author>> getAuthorsByNationality(
            @Parameter(description = "Nationality", required = true) @PathVariable String nationality) {
        List<Author> authors = authorService.findByNationality(nationality);
        return ResponseEntity.ok(authors);
    }
}
