package com.example.devopslabs.movie;

import com.example.devopslabs.movie.dto.AddMovieRequestDto;
import com.example.devopslabs.movie.dto.MovieDto;
import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
import com.example.devopslabs.movie.exception.MovieNotFoundException;
import com.example.devopslabs.movie.mapper.MovieMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    @Captor
    private ArgumentCaptor<AddMovieRequestDto> addMovieRequestDtoArgumentCaptor;

    @Captor
    private ArgumentCaptor<Movie> movieArgumentCaptor;

    @Captor
    private ArgumentCaptor<UpdateMovieRequestDto> updateMovieRequestDtoArgumentCaptor;

    @Test
    void shouldGetAllMovies() {
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        List<Movie> movies = List.of(movie1, movie2);

        when(movieRepository.findAll()).thenReturn(movies);

        MovieDto movieDto1 = new MovieDto();
        MovieDto movieDto2 = new MovieDto();

        when(movieMapper.movieToMovieDto(movie1)).thenReturn(movieDto1);
        when(movieMapper.movieToMovieDto(movie2)).thenReturn(movieDto2);

        movieService.getAllMovies();

        verify(movieRepository).findAll();
        verify(movieMapper, times(2)).movieToMovieDto(movieArgumentCaptor.capture());

        List<Movie> capturedMovies = movieArgumentCaptor.getAllValues();
        assertThat(capturedMovies).containsExactly(movie1, movie2);
    }

    @Test
    void shouldGetMovieById() {
        Integer movieId = 1;
        Movie movie = new Movie();
        movie.setId(movieId);

        MovieDto movieDto = new MovieDto();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieMapper.movieToMovieDto(movie)).thenReturn(movieDto);

        movieService.getMovieById(movieId);

        verify(movieRepository).findById(eq(movieId));
        verify(movieMapper).movieToMovieDto(movieArgumentCaptor.capture());

        Movie capturedMovie = movieArgumentCaptor.getValue();
        assertThat(capturedMovie).isEqualTo(movie);
    }

    @Test
    void shouldGetMovieByIdShouldThrowException() {
        Integer movieId = 1;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(movieId));
        verify(movieRepository).findById(eq(movieId));
    }

    @Test
    void shouldAddMovie() {
        AddMovieRequestDto addMovieRequestDto = new AddMovieRequestDto(
            "Whiplash",
            "Damien Chazelle",
            2014
    );

        movieService.addMovie(addMovieRequestDto);

        verify(movieMapper).addMovieRequestDtoToMovie(addMovieRequestDtoArgumentCaptor.capture());
        verify(movieRepository).save(movieArgumentCaptor.capture());
        verify(movieMapper).movieToMovieDto(movieArgumentCaptor.capture());

        AddMovieRequestDto capturedAddMovieRequest = addMovieRequestDtoArgumentCaptor.getValue();
        assertThat(capturedAddMovieRequest).isEqualTo(addMovieRequestDto);
    }

    @Test
    void shouldDeleteMovieById() {
        Integer movieId = 1;
        Movie movie = new Movie();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        movieService.deleteMovieById(movieId);

        verify(movieRepository).findById(eq(movieId));
        verify(movieRepository).delete(movieArgumentCaptor.capture());
        verify(movieMapper).movieToMovieDto(movieArgumentCaptor.capture());

        Movie capturedMovie = movieArgumentCaptor.getValue();
        assertThat(capturedMovie).isEqualTo(movie);
    }

    @Test
    void shouldDeleteMovieByIdShouldThrowException() {
        Integer movieId = 1;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovieById(movieId));

        verify(movieRepository).findById(eq(movieId));
    }

    @Test
    void shouldUpdateMovie() {
        Integer movieId = 1;
        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto();
        updateMovieRequestDto.setId(movieId);

        Movie existingMovie = new Movie();
        Movie updatedMovie = new Movie();
        MovieDto movieDto = new MovieDto();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieMapper.updateMovieRequestDtoToMovie(updateMovieRequestDto, existingMovie)).thenReturn(updatedMovie);
        when(movieRepository.save(updatedMovie)).thenReturn(updatedMovie);
        when(movieMapper.movieToMovieDto(updatedMovie)).thenReturn(movieDto);

        movieService.updateMovie(updateMovieRequestDto);

        verify(movieRepository).findById(eq(movieId));
        verify(movieMapper).updateMovieRequestDtoToMovie(updateMovieRequestDtoArgumentCaptor.capture(), eq(existingMovie));
        verify(movieRepository).save(movieArgumentCaptor.capture());
        verify(movieMapper).movieToMovieDto(movieArgumentCaptor.capture());

        UpdateMovieRequestDto capturedUpdateMovieRequest = updateMovieRequestDtoArgumentCaptor.getValue();
        assertThat(capturedUpdateMovieRequest).isEqualTo(updateMovieRequestDto);

        Movie capturedMovie = movieArgumentCaptor.getValue();
        assertThat(capturedMovie).isEqualTo(updatedMovie);
    }

    @Test
    void updateMovieShouldThrowException() {
        Integer movieId = 1;
        UpdateMovieRequestDto updateMovieRequestDto = new UpdateMovieRequestDto();
        updateMovieRequestDto.setId(movieId);

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(updateMovieRequestDto));

        verify(movieRepository).findById(eq(movieId));
    }

    @Test
    void shouldFindByDirectorOrderByReleaseYearDesc() {
        String director = "Quentin Tarantino";
        MovieDto movieDto1 = new MovieDto();
        MovieDto movieDto2 = new MovieDto();
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        List<Movie> movies = List.of(movie1, movie2);

        when(movieRepository.findByDirectorOrderByReleaseYearDesc(director)).thenReturn(movies);
        when(movieMapper.movieToMovieDto(movie1)).thenReturn(movieDto1);
        when(movieMapper.movieToMovieDto(movie2)).thenReturn(movieDto2);

        movieService.findByDirectorOrderByReleaseYearDesc(director);

        verify(movieRepository).findByDirectorOrderByReleaseYearDesc(eq(director));
        verify(movieMapper, times(2)).movieToMovieDto(movieArgumentCaptor.capture());

        List<Movie> capturedMovies = movieArgumentCaptor.getAllValues();
        assertThat(capturedMovies).containsExactly(movie1, movie2);
    }
}
