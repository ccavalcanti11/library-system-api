package com.librarysystem.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Data
public class Book {

    @Id
    private String id;
    private String title;
    private String isbn;
    private String authorId;
    private String genre;
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer publicationYear;

}
