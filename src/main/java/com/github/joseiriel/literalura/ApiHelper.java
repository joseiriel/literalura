package com.github.joseiriel.literalura;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiHelper {
    static final String apiPath = "https://gutendex.com";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public ApiHelper() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Book> getBookList(String params) {
        var url = apiPath + "/books/";
        if (params != null) {
            url += "?" + params;
        }
        return parse(request(url), BookDataList.class).results().stream().map(Book::new).toList();
    }

    public Book getBook(int id) {
        return new Book(parse(request(apiPath + "/book/" + id), BookData.class));
    }

    public List<Book> searchBooks(String query) {
        return getBookList("search="+ query.replace(" ", "+"));
    }

    public <T> T parse(String json, Class<T> c) {
        try {
            return mapper.readValue(json, c);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> parseList(String json, Class<T> c) {
        CollectionType list = mapper.getTypeFactory()
                .constructCollectionType(List.class, c);
        try {
            return mapper.readValue(json, list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String request(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        //TODO: do something with the response status code
        return response.body();
    }
}
