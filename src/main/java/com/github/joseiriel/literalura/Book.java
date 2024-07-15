package com.github.joseiriel.literalura;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Book {
    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    public String title;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    public List<String> languages;
    public int downloadCount;

    public Book() {}

    public Book(BookData data) {
        this(data, new Author(data.authors().get(0)));
    }

    public Book(BookData data, Author author) {
        title = data.title();
        languages = data.languages();
        downloadCount = data.downloadCount();
        this.author = author;
    }

    public Author author() {
        return author;
    }

    public void setAuthor(Author a) {
        author = a;
    }

    @Override
    public String toString() {
        return String.format("'%s'; %s", title, (author != null) ? author : "autor desconhecido");
    }
}
