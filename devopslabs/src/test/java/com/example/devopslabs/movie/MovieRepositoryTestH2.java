//package com.example.devopslabs.movie;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Disabled
//class MovieRepositoryTestH2 {
//
//    @Autowired
//    private MovieRepository underTest;
//
//    @AfterEach
//    void tearDown() {
//        underTest.deleteAll();
//    }
//
//    @Test
//    @DisplayName("Should find movies by director and order them by release year in descending order")
//    void shouldFindMoviesByDirectorOrderedByReleaseYearDesc() {
//        String director = "Martin Scorsese";
//
//        Movie movie1 = new Movie("Goodfellas", director, 1990);
//        Movie movie2 = new Movie("Raging Bull", director, 1980);
//
//        movie1 = underTest.save(movie1);
//        movie2 = underTest.save(movie2);
//
//        List<Movie> testMovies = underTest.findByDirectorOrderByReleaseYearDesc(director);
//
//        assertThat(testMovies).containsExactly(movie1, movie2);
//    }
//
//    @Test
//    void shouldFindMoviesByDirectorOrderedByReleaseYearDescWithDifferentDirectors() {
//        String director1 = "Martin Scorsese";
//        String director2 = "Quentin Tarantino";
//
//        Movie movie1 = new Movie("Goodfellas", director1, 1990);
//        Movie movie2 = new Movie("Raging Bull", director1, 1980);
//        Movie movie3 = new Movie("Pulp Fiction", director2, 1994);
//
//        movie1 = underTest.save(movie1);
//        movie2 = underTest.save(movie2);
//        movie3 = underTest.save(movie3);
//
//        List<Movie> testMoviesDirector1 = underTest.findByDirectorOrderByReleaseYearDesc(director1);
//        List<Movie> testMoviesDirector2 = underTest.findByDirectorOrderByReleaseYearDesc(director2);
//
//        assertThat(testMoviesDirector1).containsExactly(movie1, movie2);
//        assertThat(testMoviesDirector2).containsExactly(movie3);
//    }
//}
