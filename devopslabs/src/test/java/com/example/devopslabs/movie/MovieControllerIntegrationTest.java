package com.example.devopslabs.movie;

import com.example.devopslabs.movie.dto.AddMovieRequestDto;
import com.example.devopslabs.movie.dto.MovieDto;
import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    private List<Movie> movies;

    @BeforeEach
    void beforeEach() {
        movies = List.of(
                new Movie("The Shawshank Redemption", "Frank Darabont", 1994),
                new Movie("The Godfather", "Francis Ford Coppola", 1972),
                new Movie("Pulp Fiction", "Quentin Tarantino", 1994)
        );

        movies = movieRepository.saveAll(movies);
    }

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldGetAllMovies() {
        ResponseEntity<MovieDto[]> responseEntity = restTemplate.exchange("/movie", HttpMethod.GET, null, MovieDto[].class);
        MovieDto[] moviesDto = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(moviesDto).isNotEmpty();
        assertThat(moviesDto.length).isEqualTo(3);
    }

    @Test
    void shouldGetMovieById() {
        Integer movieId = movies.get(0).getId();
        ResponseEntity<MovieDto> responseEntity = restTemplate.exchange("/movie/{id}", HttpMethod.GET, null, MovieDto.class, movieId);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void shouldThrowNotFoundWhenGetMovieByInvalidId() {
        ResponseEntity<MovieDto> responseEntity = restTemplate.exchange("/movie/-2", HttpMethod.GET, null, MovieDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldThrowBadREquestWhenGetMovieByBadId() {
        ResponseEntity<MovieDto> responseEntity = restTemplate.exchange("/movie/badParam", HttpMethod.GET, null, MovieDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    void shouldAddMovie() {
        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto("Goodfellas", "Martin Scorsese", 1990);
        ResponseEntity<MovieDto> response = restTemplate.postForEntity("/movie", addMovieRequestDto, MovieDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Goodfellas");
        assertThat(response.getBody().getDirector()).isEqualTo("Martin Scorsese");
        assertThat(response.getBody().getReleaseYear()).isEqualTo(1990);

        List<Movie> updatedMovies = movieRepository.findAll();
        assertThat(updatedMovies).hasSize(movies.size() + 1);
        assertThat(updatedMovies).extracting("title").contains("Goodfellas");

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getCreatedAt()).isNotNull();
        assertThat(response.getBody().getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldNotAddMovie() {
        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto();
        ResponseEntity<MovieDto> response = restTemplate.postForEntity("/movie", addMovieRequestDto, MovieDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotAddMovieBecauseAlreadyExists() {
        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto(movies.get(0).getTitle(), movies.get(0).getDirector(), movies.get(0).getReleaseYear());
        ResponseEntity<MovieDto> response = restTemplate.postForEntity("/movie", addMovieRequestDto, MovieDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @Rollback
    public void shouldDeleteMovie() {
        Integer movieId = movies.get(0).getId();
        ResponseEntity<MovieDto> response = restTemplate.exchange("/movie/{id}", HttpMethod.DELETE, null, MovieDto.class, movieId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(movieId);

        List<Movie> updatedMovies = movieRepository.findAll();
        assertThat(updatedMovies).hasSize(movies.size() - 1);
    }

    @Test
    public void shouldNotDeleteMovie() {
        ResponseEntity<MovieDto> response = restTemplate.exchange("/movie/-2", HttpMethod.DELETE, null, MovieDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldNotDeleteMovieBadParameter() {
        ResponseEntity<MovieDto> response = restTemplate.exchange("/movie/badParam", HttpMethod.DELETE, null, MovieDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    public void shouldUpdateMovie() {
        Integer movieId = movies.get(0).getId();
        Date updatedAt = movies.get(0).getUpdatedAt();
        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto(movieId, "Updated Title", "NewDirector", 1999);
        HttpEntity<UpdateMovieRequestDto> requestEntity = new HttpEntity<>(updateMovieRequestDto);
        ResponseEntity<MovieDto> response = restTemplate.exchange("/movie", HttpMethod.PUT, requestEntity, MovieDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(movieId);
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
        assertThat(response.getBody().getUpdatedAt()).isNotEqualTo(updatedAt);

        Optional<Movie> updatedMovie = movieRepository.findById(movieId);
        assertThat(updatedMovie).isNotNull();
        updatedMovie.ifPresent(movie -> {
            assertThat(movie.getTitle()).isEqualTo("Updated Title");
        });
    }

    @Test
    public void shouldNotFoundUpdateMovie() {
        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto(-2, "Updated Title", "NewDirector", 1999);
        HttpEntity<UpdateMovieRequestDto> requestEntity = new HttpEntity<>(updateMovieRequestDto);
        ResponseEntity<MovieDto> response = restTemplate.exchange("/movie", HttpMethod.PUT, requestEntity, MovieDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldNotUpdateMovie() {
        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto();
        HttpEntity<UpdateMovieRequestDto> requestEntity = new HttpEntity<>(updateMovieRequestDto);
        ResponseEntity<MovieDto> response = restTemplate.exchange("/movie", HttpMethod.PUT, requestEntity, MovieDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldFindByDirectorOrderByReleaseYearDesc() {
        String director = "Frank Darabont";
        ResponseEntity<MovieDto[]> responseEntity = restTemplate.exchange("/movie/director/{director}", HttpMethod.GET, null, MovieDto[].class, director);
        MovieDto[] moviesDto = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(moviesDto).isNotEmpty();
        assertThat(moviesDto.length).isEqualTo(1);
        assertThat(moviesDto[0].getDirector()).isEqualTo(director);
    }

    @Test
    public void shouldFindEmptyListByDirectorOrderByReleaseYearDesc() {
        ResponseEntity<MovieDto[]> responseEntity = restTemplate.exchange("/movie/director/Nolan", HttpMethod.GET, null, MovieDto[].class);
        MovieDto[] moviesDto = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(moviesDto).isEmpty();
    }
}
