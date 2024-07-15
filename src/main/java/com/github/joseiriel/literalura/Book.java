package com.github.joseiriel.literalura;

import java.util.List;
import java.util.Optional;

public class Book {
        public String title;
        public Optional<Author> author;
        public List<String> languages;
        public int downloadCount;

    public Book(BookData data) {
        title = data.title();
        if (!data.authors().isEmpty()) {
            author = Optional.of(Author.fromData(data.authors().get(0)));
        } else {
            author = Optional.empty();
        }
        languages = data.languages();
        downloadCount = data.downloadCount();
    }

    @Override
    public String toString() {
        return String.format("'%s'; %s", title, author.isPresent() ? author.get() : "autor desconhecido");
    }
}
