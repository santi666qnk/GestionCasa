package com.smc.casa.service;

import com.smc.casa.model.Book;
import com.smc.casa.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long bookId) {
         Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (!bookOptional.isPresent()) {
            throw new IllegalStateException("Tarea " + bookId + " no existe.");
        }
        return bookRepository.findById(bookId);
    }

    public void addNewBook(Book book) {
        validateISBN(book.getIsbn());
        validateTitle(book.getTitle());
        validateAuthor(book.getAuthor());
        validateBookStatus(book.getStatus());
        book.setStatus(book.getStatus().toUpperCase());
        Optional<Book> bookOptional = bookRepository.findBookByIsbn(book.getIsbn());
        if (bookOptional.isPresent()) {
            throw new IllegalStateException(
                    "El Id " + book.getIsbn() + " esta en uso."
            );
        }
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(Long bookId, String isbn, String title, String author, String status) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalStateException(
                "Tarea con Id " + bookId + " no existe."
        ));

        if (valueHasBeenChanged(isbn, book.getIsbn())) {
            validateISBN(isbn);
            book.setIsbn(isbn);
        }

        if (valueHasBeenChanged(title, book.getTitle())) {
            validateTitle(title);
            book.setTitle(title);
        }

        if (valueHasBeenChanged(author, book.getAuthor())) {
            validateAuthor(author);
            book.setAuthor(author);
        }

        if (valueHasBeenChanged(status, book.getStatus())) {
            validateBookStatus(status);
            book.setStatus(status.toUpperCase());
        }
    }

    private boolean valueHasBeenChanged(String newVal, String originalVal) {
        return newVal != null && newVal.length() > 0 && !Objects.equals(newVal, originalVal);
    }

    private void validateBookStatus(String status) {
        if (status.trim().length() == 0) {
            throw new IllegalArgumentException("El Estado no puede estar en blanco.");
        }
        if (!(status.toLowerCase().equals("terminado")
                || status.toLowerCase().equals("pendiente"))) {
            throw new IllegalArgumentException("\"" + status + "\"" + " no es un estado valido. 'TERMINADO|PENDIENTE");
        }
    }

    private void validateAuthor(String author) {
        if (author.trim().length() == 0) {
            throw new IllegalArgumentException("El Autor no puede estar en blanco.");
        }
    }

    private void validateTitle(String title) {
        if (title.trim().length() == 0) {
            throw new IllegalArgumentException("El Título no puede estar en blanco.");
        }
    }

    private void validateISBN(String isbn) {
        if (isbn.trim().length() == 0) {
            throw new IllegalArgumentException("El id no puede estar en blanco.");
        }

        String isbnRegex = "\\d{4}-(PATIO-1|PATIO-2|COCINA|SALON|WC|ESTUDIO|DORMITORIO-1|DORMITORIO-2|COCHE)";

        Pattern pattern = Pattern.compile(isbnRegex);
        Matcher matcher = pattern.matcher(isbn);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("El Id no esta escrito con el formato correcto: '0001-PATIO-1|PATIO-2|COCINA|SALON|WC|ESTUDIO|DORMITORIO-1|DORMITORIO-2|COCHE'");
        }

        Optional<Book> bookOptional = bookRepository.findBookByIsbn(isbn);
        if (bookOptional.isPresent()) {
            throw new IllegalStateException("Id en uso. No pueden coincidir dos id de diferentes tareas.");
        }
    }

    public void deleteBook(Long bookId) {
        boolean exists = bookRepository.existsById(bookId);
        if (!exists) {
            throw new IllegalStateException("Tarea con id " + bookId + " no existe.");
        }
        bookRepository.deleteById(bookId);
    }

    public Page<Book> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.bookRepository.findAll(pageable);
    }
}
