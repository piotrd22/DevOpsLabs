package com.example.devopslabs.movie.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Integer id) {
        super("Movie with id '%s' not found".formatted(id));
    }
}
