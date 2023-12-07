package com.example.devopslabs.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private Integer id;
    private String title;
    private String director;
    private Integer releaseYear;
    private Date createdAt;
    private Date updatedAt;
}
