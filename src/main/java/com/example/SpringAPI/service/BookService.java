package com.example.SpringAPI.service;

import com.example.SpringAPI.api.model.Book;
import com.example.SpringAPI.api.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    //mock data
    @PostConstruct
    public void init() {
        bookRepository.save(new Book("Shadow and Bone", "Leigh bardugo", 2012, 4.5));
        bookRepository.save(new Book("Siege and Storm", "Leigh bardugo", 2013, 4.0));
        bookRepository.save(new Book("Ruin and Rising", "Leigh bardugo", 2014, 3.5));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> filterBooks(String title, String author, Integer year, Double rating) {
        if (title != null) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        } else if (author != null) {
            return bookRepository.findByAuthorContainingIgnoreCase(author);
        } else if (year != null) {
            return bookRepository.findByYear(year);
        } else if (rating != null) {
            return bookRepository.findByRating(rating);
        }
        return getAllBooks();
    }

    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public Book rateBook(Long id, double rating) throws IllegalArgumentException {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setRating(rating);
            return bookRepository.save(book);
        }
        return null;
    }
}
