package com.example.devopslabs.movie.mapper;

import com.example.devopslabs.movie.Movie;
import com.example.devopslabs.movie.dto.AddMovieRequestDto;
import com.example.devopslabs.movie.dto.MovieDto;
import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class MovieMapperTest {

    private final MovieMapper movieMapper = Mappers.getMapper(MovieMapper.class);

    @Test
    void shouldMapMovieDtoToNullMovie() {
        MovieDto movie = movieMapper.movieToMovieDto(null);
        assertThat(movie).isNull();
    }

    @Test
    void shouldMapMovieToMovieDto() {
        Date createdAt = new Date();
        Date updatedAt = new Date();
        Movie movie = new Movie(1, "The Shawshank Redemption", "Frank Darabont", 1994, createdAt, updatedAt);

        MovieDto movieDto = movieMapper.movieToMovieDto(movie);

        assertThat(movieDto.getId()).isEqualTo(1);
        assertThat(movieDto.getTitle()).isEqualTo("The Shawshank Redemption");
        assertThat(movieDto.getDirector()).isEqualTo("Frank Darabont");
        assertThat(movieDto.getReleaseYear()).isEqualTo(1994);
        assertThat(movieDto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(movieDto.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldMapAddMovieRequestDtoToMovie() {
        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto("Pulp Fiction", "Quentin Tarantino", 1994);

        Movie movie = movieMapper.addMovieRequestDtoToMovie(addMovieRequestDto);

        assertThat(movie.getTitle()).isEqualTo("Pulp Fiction");
        assertThat(movie.getDirector()).isEqualTo("Quentin Tarantino");
        assertThat(movie.getReleaseYear()).isEqualTo(1994);
        assertThat(movie.getId()).isNull();
        assertThat(movie.getCreatedAt()).isNull();
        assertThat(movie.getUpdatedAt()).isNull();
    }

    @Test
    void shouldMapNullAddMovieRequestDtoToMovie() {
        Movie movie = movieMapper.addMovieRequestDtoToMovie(null);
        assertThat(movie).isNull();
    }

    @Test
    void shouldMapUpdateMovieRequestDtoToMovie() {
        int id = 2;
        Date createdAt = new Date();
        Date updatedAt = new Date();
        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto(id, "The Godfather", "Francis Ford Coppola", 1972);
        Movie existingMovie = new Movie(id, "The Dark Knight", "Christopher Nolan", 2008, createdAt, updatedAt);

        Movie updatedMovie = movieMapper.updateMovieRequestDtoToMovie(updateMovieRequestDto, existingMovie);

        assertThat(updatedMovie.getId()).isEqualTo(id);
        assertThat(updatedMovie.getTitle()).isEqualTo("The Godfather");
        assertThat(updatedMovie.getDirector()).isEqualTo("Francis Ford Coppola");
        assertThat(updatedMovie.getReleaseYear()).isEqualTo(1972);
        assertThat(updatedMovie.getCreatedAt()).isEqualTo(createdAt);
        assertThat(updatedMovie.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldMapNullUpdateMovieRequestDtoToMovie() {
        Movie movie = movieMapper.updateMovieRequestDtoToMovie(null, new Movie());
        assertThat(movie.getId()).isNull();
        assertThat(movie.getTitle()).isNull();
        assertThat(movie.getDirector()).isNull();
        assertThat(movie.getReleaseYear()).isNull();
        assertThat(movie.getCreatedAt()).isNull();
        assertThat(movie.getUpdatedAt()).isNull();
    }
}
