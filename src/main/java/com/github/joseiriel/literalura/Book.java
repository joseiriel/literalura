package com.github.joseiriel.literalura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Book(
        @JsonProperty int id,
        @JsonProperty String title,
        @JsonProperty List<Author> authors,
        @JsonProperty List<String> languages,
        @JsonProperty("download_count") int downloadCount
) {
    @Override
    public String toString() {
        return String.format("'%s'; %s", title, authors.get(0));
    }
}
