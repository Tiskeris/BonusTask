package com.example.SpringAPI;

import com.example.SpringAPI.api.model.Book;
import com.example.SpringAPI.api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc


class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    void testCreateBook() throws Exception {
        String bookJson = "{ \"title\": \"Test Book\", \"author\": \"Author Name\", \"year\": 2023, \"rating\": 4.5 }";

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    void testGetAllBooks() throws Exception {
        bookRepository.save(new Book("Book 1", "Author 1", 2023, 4.5));
        bookRepository.save(new Book("Book 2", "Author 2", 2024, 5.0));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testRateBook() throws Exception {
        Book book = bookRepository.save(new Book("Book 1", "Author 1", 2023, 4.5));

        mockMvc.perform(post("/books/" + book.getId() + "/rate?rating=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testRateBookInvalidRating() throws Exception {
        Book book = bookRepository.save(new Book("Book 1", "Author 1", 2023, 4.5));

        mockMvc.perform(post("/books/" + book.getId() + "/rate?rating=6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(" Rating must be between 1 and 5."));
    }
}
