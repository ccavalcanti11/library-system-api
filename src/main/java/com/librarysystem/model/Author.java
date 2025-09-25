package com.librarysystem.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "authors")
@Data
public class Author {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    private String biography;
    private String nationality;
    private LocalDate birthDate;
    private String website;
    private List<String> genres;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

}
