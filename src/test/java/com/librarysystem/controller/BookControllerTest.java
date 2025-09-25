package com.librarysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarysystem.model.Book;
import com.librarysystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId("1");
        testBook.setTitle("Test Book");
        testBook.setIsbn("978-0123456789");
        testBook.setAuthorId("author1");
        testBook.setGenre("Fiction");
        testBook.setTotalCopies(5);
        testBook.setAvailableCopies(3);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        // Given
        List<Book> books = Collections.singletonList(testBook);
        when(bookService.findAll()).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Book"))
                .andExpect(jsonPath("$[0].isbn").value("978-0123456789"));

        verify(bookService).findAll();
    }

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() throws Exception {
        // Given
        when(bookService.findById("1")).thenReturn(Optional.of(testBook));

        // When & Then
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.isbn").value("978-0123456789"));

        verify(bookService).findById("1");
    }

    @Test
    void getBookById_ShouldReturn404_WhenBookNotFound() throws Exception {
        // Given
        when(bookService.findById("999")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());

        verify(bookService).findById("999");
    }

    @Test
    void createBook_ShouldReturnCreatedBook_WhenValidBook() throws Exception {
        // Given
        when(bookService.save(any(Book.class))).thenReturn(testBook);

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService).save(any(Book.class));
    }

    @Test
    void createBook_ShouldReturn400_WhenInvalidBook() throws Exception {
        // Given
        when(bookService.save(any(Book.class))).thenThrow(new RuntimeException("Invalid book"));

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook_WhenBookExists() throws Exception {
        // Given
        testBook.setTitle("Updated Title");
        when(bookService.findById("1")).thenReturn(Optional.of(testBook));
        when(bookService.save(any(Book.class))).thenReturn(testBook);

        // When & Then
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(bookService).findById("1");
        verify(bookService).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldReturn404_WhenBookNotFound() throws Exception {
        // Given
        when(bookService.findById("999")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isNotFound());

        verify(bookService).findById("999");
        verify(bookService, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldReturn204_WhenBookExists() throws Exception {
        // Given
        when(bookService.findById("1")).thenReturn(Optional.of(testBook));
        doNothing().when(bookService).deleteById("1");

        // When & Then
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).findById("1");
        verify(bookService).deleteById("1");
    }

    @Test
    void deleteBook_ShouldReturn404_WhenBookNotFound() throws Exception {
        // Given
        when(bookService.findById("999")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound());

        verify(bookService).findById("999");
        verify(bookService, never()).deleteById(any());
    }

    @Test
    void searchBooks_ShouldReturnMatchingBooks() throws Exception {
        // Given
        List<Book> books = Collections.singletonList(testBook);
        when(bookService.searchBooks("Test")).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/books/search")
                .param("keyword", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));

        verify(bookService).searchBooks("Test");
    }

    @Test
    void checkBookAvailability_ShouldReturnTrue_WhenBookAvailable() throws Exception {
        // Given
        when(bookService.isBookAvailable("1")).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/books/1/availability"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(bookService).isBookAvailable("1");
    }

    @Test
    void checkBookAvailability_ShouldReturnFalse_WhenBookNotAvailable() throws Exception {
        // Given
        when(bookService.isBookAvailable("1")).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/books/1/availability"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(bookService).isBookAvailable("1");
    }
}
