package com.librarysystem.controller;

import com.librarysystem.model.Book;
import com.librarysystem.service.BookService;
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
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management operations")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a list of all books")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book found"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "Book ID", required = true) @PathVariable String id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Add a new book to the library")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Book created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid book data")
    })
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book updated successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "400", description = "Invalid book data")
    })
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "Book ID", required = true) @PathVariable String id,
            @Valid @RequestBody Book book) {
        return bookService.findById(id)
                .map(existingBook -> {
                    book.setId(id);
                    Book updatedBook = bookService.save(book);
                    return ResponseEntity.ok(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Remove a book from the library")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID", required = true) @PathVariable String id) {
        return bookService.findById(id)
                .map(book -> {
                    bookService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search for books by keyword")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<List<Book>> searchBooks(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<Book> books = bookService.searchBooks(keyword);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Get book by ISBN", description = "Retrieve a book by its ISBN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book found"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Book> getBookByIsbn(
            @Parameter(description = "Book ISBN", required = true) @PathVariable String isbn) {
        return bookService.findByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get books by author", description = "Retrieve all books by a specific author")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    public ResponseEntity<List<Book>> getBooksByAuthor(
            @Parameter(description = "Author ID", required = true) @PathVariable String authorId) {
        List<Book> books = bookService.findByAuthorId(authorId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Check book availability", description = "Check if a book is available for borrowing")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Availability check completed"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Boolean> checkBookAvailability(
            @Parameter(description = "Book ID", required = true) @PathVariable String id) {
        boolean isAvailable = bookService.isBookAvailable(id);
        return ResponseEntity.ok(isAvailable);
    }
}
