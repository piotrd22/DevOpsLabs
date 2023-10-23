package com.example.devopslabs.movie;

import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.devopslabs.movie.dto.AddMovieRequestDto;
import com.example.devopslabs.movie.dto.MovieDto;
import com.example.devopslabs.movie.exception.MovieNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MovieDto movieDto;

    @BeforeEach
    void setUp() {
        movieDto = new MovieDto(
                1,
                "Whiplash",
                "Damien Chazelle",
                2014,
                new Date(),
                new Date()
        );
    }

    @Test
    void shouldGetAllMovies() throws Exception {
        when(movieService.getAllMovies()).thenReturn(Collections.singletonList(movieDto));
        mockMvc.perform(get("/movie"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(movieDto.getId())));
    }

    @Test
    void shouldHandleEmptyGetAllMovies() throws Exception {
        when(movieService.getAllMovies()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/movie"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldGetMovieById() throws Exception {
        when(movieService.getMovieById(1)).thenReturn(movieDto);
        mockMvc.perform(get("/movie/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(movieDto.getId())));
    }

    @Test
    void shouldHandleMovieNotFoundGetMovieById() throws Exception {
        when(movieService.getMovieById(2)).thenThrow(new MovieNotFoundException(2));
        mockMvc.perform(get("/movie/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddMovie() throws Exception {
        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto(
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getReleaseYear()
        );

        when(movieService.addMovie(eq(addMovieRequestDto))).thenReturn(movieDto);

        mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addMovieRequestDto))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(movieDto.getId())))
                .andExpect(jsonPath("$.title", is(movieDto.getTitle())))
                .andExpect(jsonPath("$.director", is(movieDto.getDirector())))
                .andExpect(jsonPath("$.releaseYear", is(movieDto.getReleaseYear())));
    }

    @Test
    void shouldHandleInvalidAddMovieRequest() throws Exception {
        AddMovieRequestDto invalidRequest = new AddMovieRequestDto("Movie Title", null, 2023);
        mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteMovieById() throws Exception {
        when(movieService.deleteMovieById(1)).thenReturn(movieDto);
        mockMvc.perform(delete("/movie/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(movieDto.getId())));
    }

    @Test
    void shouldHandleMovieDeletionError() throws Exception {
        when(movieService.deleteMovieById(2)).thenThrow(new MovieNotFoundException(2));
        mockMvc.perform(delete("/movie/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateMovie() throws Exception {
        String updatedTitle = "Updated Title";
        String updatedDirector = "Updated Director";
        int updatedReleaseYear = 2022;

        MovieDto updatedMovie = new MovieDto(
                movieDto.getId(),
                updatedTitle,
                updatedDirector,
                updatedReleaseYear,
                movieDto.getCreatedAt(),
                new Date()
        );

        UpdateMovieRequestDto updateRequest = new UpdateMovieRequestDto(movieDto.getId(), updatedTitle, updatedDirector, updatedReleaseYear);
        when(movieService.updateMovie(updateRequest)).thenReturn(updatedMovie);

        mockMvc.perform(put("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(movieDto.getId())))
                .andExpect(jsonPath("$.title", is(updateRequest.getTitle())))
                .andExpect(jsonPath("$.director", is(updateRequest.getDirector())))
                .andExpect(jsonPath("$.releaseYear", is(updateRequest.getReleaseYear())));
    }

    @Test
    void shouldFindMoviesByDirectorOrderedByReleaseYearDesc() throws Exception {
        String director = "Christopher Nolan";

        MovieDto movie1 = new MovieDto(1, "Inception", director, 2010, new Date(), new Date());
        MovieDto movie2 = new MovieDto(2, "Interstellar", director, 2014, new Date(), new Date());

        List<MovieDto> expectedMovies = List.of(movie2, movie1);

        when(movieService.findByDirectorOrderByReleaseYearDesc(eq(director))).thenReturn(expectedMovies);

        mockMvc.perform(get("/movie/director/{director}", director))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(movie2.getId())))
                .andExpect(jsonPath("$[0].title", is(movie2.getTitle())))
                .andExpect(jsonPath("$[0].director", is(movie2.getDirector())))
                .andExpect(jsonPath("$[0].releaseYear", is(movie2.getReleaseYear())))
                .andExpect(jsonPath("$[1].id", is(movie1.getId())))
                .andExpect(jsonPath("$[1].title", is(movie1.getTitle())))
                .andExpect(jsonPath("$[1].director", is(movie1.getDirector())))
                .andExpect(jsonPath("$[1].releaseYear", is(movie1.getReleaseYear())));
    }
}
