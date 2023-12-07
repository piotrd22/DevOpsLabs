package com.example.devopslabs.movie;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @SequenceGenerator(
            name = "movie_id_sequence",
            sequenceName = "movie_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movie_id_sequence"
    )
    private Integer id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String director;

    @NotNull
    @Column(nullable = false)
    private Integer releaseYear;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        updatedAt = createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public Movie(String title, String director, Integer releaseYear) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
    }
}
