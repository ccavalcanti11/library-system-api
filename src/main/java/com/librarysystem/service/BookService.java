package com.librarysystem.service;

import com.librarysystem.model.Book;
import com.librarysystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(String id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByKeyword(keyword);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findByAuthorId(String authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public boolean isBookAvailable(String bookId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        return bookOpt.map(book -> book.getAvailableCopies() != null && book.getAvailableCopies() > 0)
                .orElse(false);
    }

    public void borrowBook(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies of this book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
    }

    public void returnBook(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Ensure we don't exceed total copies
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(1);
        } else if (book.getTotalCopies() == null || book.getAvailableCopies() < book.getTotalCopies()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
        }

        bookRepository.save(book);
    }
}
