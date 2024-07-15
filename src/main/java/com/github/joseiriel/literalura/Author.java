package com.github.joseiriel.literalura;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public final class Author {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    public Author(AuthorData data) {
        name = data.name();
        birthYear = data.birthYear();
        deathYear = data.deathYear();
    }

    @Override
    public String toString() {
        return String.format("%s (%s-%s)", name, birthYear != null ? birthYear : "?", deathYear != null ? deathYear : "?");
    }

    @Id
    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Integer birthYear() {
        return birthYear;
    }

    public Integer deathYear() {
        return deathYear;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Author) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.birthYear, that.birthYear) &&
                Objects.equals(this.deathYear, that.deathYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthYear, deathYear);
    }

}
