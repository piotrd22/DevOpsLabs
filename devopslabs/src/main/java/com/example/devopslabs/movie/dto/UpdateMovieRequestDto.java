package com.example.devopslabs.movie.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMovieRequestDto {
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @NotBlank
    private String title;

    @NotBlank
    private String director;

    @Min(value = 1800, message = "Year should not be less than 1800")
    private Integer releaseYear;
}
