package com.example.devopslabs.movie.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class AddMovieRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String director;


    @NotNull(message = "Year cannot be null")
    @Min(value = 1800, message = "Year should not be less than 1800")
    private Integer year;
}
