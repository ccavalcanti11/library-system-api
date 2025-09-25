package com.librarysystem.service;

import com.librarysystem.model.Book;
import com.librarysystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId("1");
        testBook.setTitle("Test Book");
        testBook.setIsbn("978-0123456789");
        testBook.setAuthorId("author1");
        testBook.setTotalCopies(5);
        testBook.setAvailableCopies(3);
    }

    @Test
    void findById_ShouldReturnBook_WhenBookExists() {
        // Given
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));

        // When
        Optional<Book> result = bookService.findById("1");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        verify(bookRepository).findById("1");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenBookDoesNotExist() {
        // Given
        when(bookRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<Book> result = bookService.findById("999");

        // Then
        assertFalse(result.isPresent());
        verify(bookRepository).findById("999");
    }

    @Test
    void save_ShouldSaveBook_WhenValidBook() {
        // Given
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        Book result = bookService.save(testBook);

        // Then
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository).save(testBook);
    }

    @Test
    void isBookAvailable_ShouldReturnTrue_WhenCopiesAvailable() {
        // Given
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));

        // When
        boolean result = bookService.isBookAvailable("1");

        // Then
        assertTrue(result);
        verify(bookRepository).findById("1");
    }

    @Test
    void isBookAvailable_ShouldReturnFalse_WhenNoCopiesAvailable() {
        // Given
        testBook.setAvailableCopies(0);
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));

        // When
        boolean result = bookService.isBookAvailable("1");

        // Then
        assertFalse(result);
        verify(bookRepository).findById("1");
    }

    @Test
    void borrowBook_ShouldDecrementAvailableCopies_WhenBookAvailable() {
        // Given
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));

        // When
        bookService.borrowBook("1");

        // Then
        assertEquals(2, testBook.getAvailableCopies());
        verify(bookRepository).save(testBook);
    }

    @Test
    void borrowBook_ShouldThrowException_WhenNoAvailableCopies() {
        // Given
        testBook.setAvailableCopies(0);
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookService.borrowBook("1"));

        assertTrue(exception.getMessage().contains("No available copies"));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void returnBook_ShouldIncrementAvailableCopies_WhenValidReturn() {
        // Given
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));

        // When
        bookService.returnBook("1");

        // Then
        assertEquals(4, testBook.getAvailableCopies());
        verify(bookRepository).save(testBook);
    }

    @Test
    void searchBooks_ShouldReturnMatchingBooks() {
        // Given
        List<Book> expectedBooks = Collections.singletonList(testBook);
        when(bookRepository.findByKeyword("Test")).thenReturn(expectedBooks);

        // When
        List<Book> result = bookService.searchBooks("Test");

        // Then
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        verify(bookRepository).findByKeyword("Test");
    }

    @Test
    void deleteById_ShouldDeleteBook_WhenBookExists() {
        // Given
        when(bookRepository.existsById("1")).thenReturn(true);

        // When
        bookService.deleteById("1");

        // Then
        verify(bookRepository).deleteById("1");
    }

    @Test
    void deleteById_ShouldThrowException_WhenBookDoesNotExist() {
        // Given
        when(bookRepository.existsById("999")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookService.deleteById("999"));

        assertEquals("Book not found with id: 999", exception.getMessage());
        verify(bookRepository, never()).deleteById(any());
    }
}
