package com.librarysystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarysystem.model.Book;
import com.librarysystem.model.Author;
import com.librarysystem.repository.BookRepository;
import com.librarysystem.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWebMvc
public class BookIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withExposedPorts(27017);

    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
        // Disable cache for integration tests to avoid Redis connection issues during testing
        registry.add("spring.cache.type", () -> "none");
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Author testAuthor;
    private Book testBook;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Clean up database
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        // Create test author
        testAuthor = new Author();
        testAuthor.setFirstName("John");
        testAuthor.setLastName("Doe");
        testAuthor.setEmail("john.doe@example.com");
        testAuthor = authorRepository.save(testAuthor);

        // Create test book
        testBook = new Book();
        testBook.setTitle("Integration Test Book");
        testBook.setIsbn("978-0123456789");
        testBook.setAuthorId(testAuthor.getId());
        testBook.setGenre("Fiction");
        testBook.setTotalCopies(5);
        testBook.setAvailableCopies(5);
    }

    @Test
    void createBook_ShouldPersistToDatabase() throws Exception {
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Book"))
                .andExpect(jsonPath("$.isbn").value("978-0123456789"));

        // Verify book was saved to database
        var savedBooks = bookRepository.findAll();
        assertEquals(1, savedBooks.size());
        assertEquals("Integration Test Book", savedBooks.getFirst().getTitle());
    }

    @Test
    void getBookById_ShouldReturnPersistedBook() throws Exception {
        // Save book to database
        Book savedBook = bookRepository.save(testBook);

        mockMvc.perform(get("/api/books/" + savedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Book"))
                .andExpect(jsonPath("$.isbn").value("978-0123456789"))
                .andExpect(jsonPath("$.authorId").value(testAuthor.getId()));
    }

    @Test
    void searchBooks_ShouldFindMatchingBooks() throws Exception {
        // Save multiple books
        testBook.setTitle("Java Programming");
        bookRepository.save(testBook);

        Book anotherBook = new Book();
        anotherBook.setTitle("Python Programming");
        anotherBook.setIsbn("978-9876543210");
        anotherBook.setAuthorId(testAuthor.getId());
        anotherBook.setGenre("Programming");
        anotherBook.setTotalCopies(3);
        anotherBook.setAvailableCopies(3);
        bookRepository.save(anotherBook);

        mockMvc.perform(get("/api/books/search")
                .param("keyword", "Programming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateBook_ShouldModifyPersistedBook() throws Exception {
        // Save book to database
        Book savedBook = bookRepository.save(testBook);

        // Update book data
        savedBook.setTitle("Updated Title");
        savedBook.setGenre("Non-Fiction");

        mockMvc.perform(put("/api/books/" + savedBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.genre").value("Non-Fiction"));

        // Verify changes in database
        Book updatedBook = bookRepository.findById(savedBook.getId()).orElse(null);
        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals("Non-Fiction", updatedBook.getGenre());
    }

    @Test
    void deleteBook_ShouldRemoveFromDatabase() throws Exception {
        // Save book to database
        Book savedBook = bookRepository.save(testBook);

        mockMvc.perform(delete("/api/books/" + savedBook.getId()))
                .andExpect(status().isNoContent());

        // Verify book was deleted from database
        assertTrue(bookRepository.findById(savedBook.getId()).isEmpty());
    }

    @Test
    void checkBookAvailability_ShouldReturnCorrectStatus() throws Exception {
        // Save book to database
        Book savedBook = bookRepository.save(testBook);

        mockMvc.perform(get("/api/books/" + savedBook.getId() + "/availability"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // Set available copies to 0 and test again
        savedBook.setAvailableCopies(0);
        bookRepository.save(savedBook);

        mockMvc.perform(get("/api/books/" + savedBook.getId() + "/availability"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
