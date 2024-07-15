package com.github.joseiriel.literalura;

import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

@Entity
public class Book {
    @Id @GeneratedValue
    private Long id;
    public String title;
    @Transient
    public Optional<Author> author;
    public List<String> languages;
    public int downloadCount;

    public Book() {}

    public Book(BookData data) {
        title = data.title();
        if (!data.authors().isEmpty()) {
            author = Optional.of(new Author(data.authors().get(0)));
        } else {
            author = Optional.empty();
        }
        languages = data.languages();
        downloadCount = data.downloadCount();
    }

    @Override
    public String toString() {
        return String.format("'%s'; %s", title, (author != null && author.isPresent()) ? author.get() : "autor desconhecido");
    }
}
