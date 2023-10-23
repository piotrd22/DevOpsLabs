//package com.example.devopslabs.movie;
//
//import com.example.devopslabs.movie.dto.AddMovieRequestDto;
//import com.example.devopslabs.movie.dto.MovieDto;
//import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.jdbc.Sql;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Disabled
//public class MovieControllerIntegrationTestH2 {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private MovieRepository movieRepository;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    private String baseUrl;
//
//    @BeforeEach
//    void setUp() {
//        StringBuilder baseUrlBuilder = new StringBuilder("http://localhost:");
//        baseUrlBuilder.append(port);
//        baseUrlBuilder.append("/movie");
//        baseUrl = baseUrlBuilder.toString();
//    }
//
//    @Test
//    public void shouldAddMovie() {
//        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto("Goodfellas", "Martin Scorsese", 1990);
//        ResponseEntity<MovieDto> response = restTemplate.postForEntity(baseUrl, addMovieRequestDto, MovieDto.class);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("Goodfellas", response.getBody().getTitle());
//        assertEquals("Martin Scorsese", response.getBody().getDirector());
//        assertEquals(1990, response.getBody().getReleaseYear());
//        assertEquals(1, movieRepository.findAll().size());
//        assertEquals("Goodfellas", movieRepository.findAll().get(0).getTitle());
//
//        assertNotNull(response.getBody().getId());
//        assertNotNull(response.getBody().getCreatedAt());
//        assertNotNull(response.getBody().getUpdatedAt());
//    }
//
//    @Test
//    public void shouldNotAddMovie() {
//        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto();
//        ResponseEntity<MovieDto> response = restTemplate.postForEntity(baseUrl, addMovieRequestDto, MovieDto.class);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//
//    @Test
//    @Sql(statements = {
//            "INSERT INTO movie (id, title, director, release_year) VALUES (1, 'The Godfather', 'Francis Ford Coppola', 1972)",
//            "INSERT INTO movie (id, title, director, release_year) VALUES (2, 'Pulp Fiction', 'Quentin Tarantino', 1994)"
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "TRUNCATE TABLE movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldGetAllMovies() {
//        ResponseEntity<List<MovieDto>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<MovieDto>>() {});
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().size() >= 2);
//
//        assertEquals("The Godfather", response.getBody().get(0).getTitle());
//        assertEquals("Pulp Fiction", response.getBody().get(1).getTitle());
//    }
//
//    @Test
//    @Sql(statements = "INSERT INTO movie (id, title, director, release_year) VALUES (1, 'Inception', 'Christopher Nolan', 2010)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "TRUNCATE TABLE movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldGetMovieById() {
//        ResponseEntity<MovieDto> response = restTemplate.getForEntity(baseUrl + "/1", MovieDto.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("Inception", response.getBody().getTitle());
//        assertEquals("Christopher Nolan", response.getBody().getDirector());
//        assertEquals(2010, response.getBody().getReleaseYear());
//    }
//
//    @Test
//    public void shouldNotGetMovieById() {
//        ResponseEntity<MovieDto> response = restTemplate.getForEntity(baseUrl + "/1", MovieDto.class);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    public void shouldNotGetMovieByIdBadParameter() {
//        ResponseEntity<MovieDto> response = restTemplate.getForEntity(baseUrl + "/badParam", MovieDto.class);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//
//    @Test
//    @Sql(statements = {
//            "INSERT INTO movie (id, title, director, release_year) VALUES (1, 'The Dark Knight', 'Christopher Nolan', 2008)",
//            "INSERT INTO movie (id, title, director, release_year) VALUES (2, 'Interstellar', 'Christopher Nolan', 2014)"
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "TRUNCATE TABLE movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldDeleteMovieById() {
//        ResponseEntity<MovieDto> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, MovieDto.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        response = restTemplate.getForEntity(baseUrl + "/1", MovieDto.class);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//
//        response = restTemplate.getForEntity(baseUrl + "/2", MovieDto.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    public void shouldNotDeleteMovieById() {
//        ResponseEntity<MovieDto> response = restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, MovieDto.class);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    @Sql(statements = "INSERT INTO movie (id, title, director, release_year) VALUES (1, 'Blade Runner', 'Ridley Scott', 1982)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "TRUNCATE TABLE movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldUpdateMovie() {
//        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto(1, "The Matrix Reloaded", "Lana Wachowski", 2003);
//        HttpEntity<UpdateMovieRequestDto> requestEntity = new HttpEntity<>(updateMovieRequestDto);
//        ResponseEntity<MovieDto> response = restTemplate.exchange(baseUrl, HttpMethod.PUT, requestEntity, MovieDto.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(1, response.getBody().getId());
//        assertEquals("The Matrix Reloaded", response.getBody().getTitle());
//        assertEquals("Lana Wachowski", response.getBody().getDirector());
//        assertEquals(2003, response.getBody().getReleaseYear());
//        assertNotNull(response.getBody().getUpdatedAt());
//    }
//
//    @Test
//    public void shouldNotFoundUpdateMovie() {
//        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto(1, "The Matrix Reloaded", "Lana Wachowski", 2003);
//        HttpEntity<UpdateMovieRequestDto> requestEntity = new HttpEntity<>(updateMovieRequestDto);
//        ResponseEntity<MovieDto> response = restTemplate.exchange(baseUrl, HttpMethod.PUT, requestEntity, MovieDto.class);
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//
//    @Test
//    public void shouldNotUpdateMovie() {
//        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto();
//        HttpEntity<UpdateMovieRequestDto> requestEntity = new HttpEntity<>(updateMovieRequestDto);
//        ResponseEntity<MovieDto> response = restTemplate.exchange(baseUrl, HttpMethod.PUT, requestEntity, MovieDto.class);
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//
//    @Test
//    @Sql(statements = {
//            "INSERT INTO movie (id, title, director, release_year) VALUES (1, 'The Dark Knight', 'Christopher Nolan', 2008)",
//            "INSERT INTO movie (id, title, director, release_year) VALUES (2, 'Inception', 'Christopher Nolan', 2010)"
//    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(statements = "TRUNCATE TABLE movie", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    public void shouldFindByDirectorOrderByReleaseYearDesc() {
//        ResponseEntity<List<MovieDto>> response = restTemplate.exchange(baseUrl + "/director/Christopher Nolan", HttpMethod.GET, null, new ParameterizedTypeReference<List<MovieDto>>() {});
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(2, response.getBody().size());
//        assertEquals("Inception", response.getBody().get(0).getTitle());
//        assertEquals("The Dark Knight", response.getBody().get(1).getTitle());
//    }
//
//    @Test
//    public void shouldNotFindByDirectorOrderByReleaseYearDesc() {
//        ResponseEntity<List<MovieDto>> response = restTemplate.exchange(baseUrl + "/director/Christopher Nolan", HttpMethod.GET, null, new ParameterizedTypeReference<List<MovieDto>>() {});
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(0, response.getBody().size());
//    }
//}
