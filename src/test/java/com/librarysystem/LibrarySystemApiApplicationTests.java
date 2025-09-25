package com.librarysystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.librarysystem.repository.AuthorRepository;
import com.librarysystem.repository.BookRepository;
import com.librarysystem.repository.BorrowerRepository;
import com.librarysystem.repository.LoanRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibrarySystemApiApplication.class)
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, RedisAutoConfiguration.class })
class LibrarySystemApiApplicationTests {

	@MockBean
	private MongoTemplate mongoTemplate;

	@MockBean
	private RedisTemplate<String, Object> redisTemplate;

	@MockBean
	private RedisConnectionFactory redisConnectionFactory;

	@MockBean
	private AuthorRepository authorRepository;

	@MockBean
	private BookRepository bookRepository;

	@MockBean
	private BorrowerRepository borrowerRepository;

	@MockBean
	private LoanRepository loanRepository;

	@Test
	void contextLoads() {
		// This test verifies that the application context loads successfully
		// External dependencies (MongoDB and Redis) are mocked to avoid connection issues
	}
}
