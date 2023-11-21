package com.smc.casa.repository;

// Data access layer

import com.smc.casa.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.isbn = ?1")
    Optional<Book> findBookByIsbn(String isbn2);

}
