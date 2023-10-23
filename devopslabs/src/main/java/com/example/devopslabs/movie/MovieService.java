package com.example.devopslabs.movie;

import com.example.devopslabs.movie.dto.AddMovieRequestDto;
import com.example.devopslabs.movie.dto.MovieDto;
import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
import com.example.devopslabs.movie.exception.*;
import com.example.devopslabs.movie.mapper.MovieMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream().map(movieMapper::movieToMovieDto).toList();
    }

    public MovieDto getMovieById(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        return movieMapper.movieToMovieDto(movie);
    }

    public MovieDto addMovie(AddMovieRequestDto dto) {
        String title = dto.getTitle();

        if (movieRepository.existsByTitle(title)) {
            throw new MovieAlreadyExistsException(title);
        }

        Movie movie = movieMapper.addMovieRequestDtoToMovie(dto);
        movie = movieRepository.save(movie);
        return movieMapper.movieToMovieDto(movie);
    }

    public MovieDto deleteMovieById(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        movieRepository.delete(movie);
        return movieMapper.movieToMovieDto(movie);
    }

    public MovieDto updateMovie(UpdateMovieRequestDto dto) {
        Integer id = dto.getId();
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        movie = movieMapper.updateMovieRequestDtoToMovie(dto, movie);
        movie = movieRepository.save(movie);
        return movieMapper.movieToMovieDto(movie);
    }

    public List<MovieDto> findByDirectorOrderByReleaseYearDesc(String director) {
        return movieRepository.findByDirectorOrderByReleaseYearDesc(director).stream().map(movieMapper::movieToMovieDto).toList();
    }
}
