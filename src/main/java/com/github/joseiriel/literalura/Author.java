package com.github.joseiriel.literalura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Author(
        @JsonProperty String name,
        @JsonProperty("birth_year") int birthYear,
        @JsonProperty("death_year") int deathYear
) {
    @Override public String toString() {
        return String.format("%s (%d-%d)", name, birthYear, deathYear);
    }
}
