package com.example.SpringAPI;

import com.example.SpringAPI.api.model.Book;
import com.example.SpringAPI.api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import com.example.SpringAPI.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Author Name");
        book.setYear(2023);
        book.setRating(4.5);
    }

    @Test
    void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.createBook(book);

        assertNotNull(createdBook);
        assertEquals("Test Book", createdBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testRateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book ratedBook = bookService.rateBook(1L, 5.0);

        assertNotNull(ratedBook);
        assertEquals(5.0, ratedBook.getRating());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testRateBookInvalidRating() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.rateBook(1L, 6);
        });

        String expectedMessage = "Rating must be between 1 and 5.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> retrievedBook = bookService.getBook(1L);

        assertTrue(retrievedBook.isPresent());
        assertEquals("Test Book", retrievedBook.get().getTitle());
    }
}