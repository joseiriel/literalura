package com.github.joseiriel.literalura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(
        @JsonProperty int id,
        @JsonProperty String title,
        @JsonProperty List<AuthorData> authors,
        @JsonProperty List<String> languages,
        @JsonProperty("download_count") int downloadCount
) {
}
