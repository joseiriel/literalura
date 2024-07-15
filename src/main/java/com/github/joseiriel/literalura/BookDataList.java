package com.github.joseiriel.literalura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDataList(
        int count,
        List<BookData> results
) {
}
