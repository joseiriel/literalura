package com.github.joseiriel.literalura;

public record Author(
        String name,
        Integer birthYear,
        Integer deathYear
) {
    public static Author fromData(AuthorData data) {
        return new Author(data.name(), data.birthYear(), data.deathYear());
    }

    @Override public String toString() {
        return String.format("%s (%s-%s)", name, birthYear != null ? birthYear : "?", deathYear != null ? deathYear : "?");
    }
}
