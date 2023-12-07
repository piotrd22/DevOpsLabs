package com.example.devopslabs.movie.exception;

public class MovieAlreadyExistsException extends RuntimeException {
    public MovieAlreadyExistsException(String title) {
        super("Movie with title '%s' already exists".formatted(title));
    }
}
