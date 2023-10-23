package com.example.devopslabs.movie;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    MovieRepository movieRepository;

    private Movie movie1;
    private Movie movie2;
    private String director;

    @BeforeEach
    void setUp() {
        director = "Martin Scorsese";
        movie1 = new Movie("Raging Bull", director, 1980);
        movie2 = new Movie("Goodfellas", director, 1990);
        movieRepository.saveAll(List.of(movie1, movie2));
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
    void shouldExistsByTitle() {
        Boolean exists = movieRepository.existsByTitle(movie1.getTitle());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldNotExistsByTitle() {
        Boolean exists = movieRepository.existsByTitle("Not existing movie");
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should find movies by director and order them by release year in descending order")
    void shouldFindMoviesByDirectorOrderedByReleaseYearDesc() {
        List<Movie> testMovies = movieRepository.findByDirectorOrderByReleaseYearDesc(director);
        assertThat(testMovies).containsExactly(movie2, movie1);
    }

    @Test
    void shouldFindMoviesByDirectorOrderedByReleaseYearDescWithDifferentDirectors() {
        String newDirector = "Quentin Tarantino";

        Movie movie3 = new Movie("Pulp Fiction", newDirector, 1994);
        movie3 = movieRepository.save(movie3);

        List<Movie> testMoviesDirector1 = movieRepository.findByDirectorOrderByReleaseYearDesc(director);
        List<Movie> testMoviesDirector2 = movieRepository.findByDirectorOrderByReleaseYearDesc(newDirector);

        assertThat(testMoviesDirector1).containsExactly(movie2, movie1);
        assertThat(testMoviesDirector2).containsExactly(movie3);
    }
}
