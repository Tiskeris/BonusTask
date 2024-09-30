package com.example.SpringAPI.api.controller;

import com.example.SpringAPI.api.model.Book;
import com.example.SpringAPI.service.BookService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
example usage to get all the books using postman:
Method: GET URL: http://localhost:8080/books

Method: GET to filter the books:
URL: http://localhost:8080/books/filter?title=Book
URL: http://localhost:8080/books/filter?author=Author
URL: http://localhost:8080/books/filter?year=2001
URL: http://localhost:8080/books/filter?rating=4.5

Method: POST to change a rating of a book:
URL: http://localhost:8080/books/1/rate?rating=4.0
*/
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/filter")
    public List<Book> filterBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) Integer year,
                                  @RequestParam(required = false) Double rating) {
        return bookService.filterBooks(title, author, year, rating);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBook(id).orElse(null);
    }

    @PostMapping("/{id}/rate")
    public Book rateBook(@PathVariable Long id, @RequestParam double rating) {
        return bookService.rateBook(id, rating);
    }
}
