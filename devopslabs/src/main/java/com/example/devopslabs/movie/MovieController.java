package com.example.devopslabs.movie;

import com.example.devopslabs.movie.dto.AddMovieRequestDto;
import com.example.devopslabs.movie.dto.MovieDto;
import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Integer id) {
        return movieService.getMovieById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MovieDto addMovie(@Valid @RequestBody AddMovieRequestDto dto) {
        return movieService.addMovie(dto);
    }

    @DeleteMapping("/{id}")
    public MovieDto deleteMovieById(@PathVariable Integer id) {
        return movieService.deleteMovieById(id);
    }

    @PutMapping
    public MovieDto updateMovie(@Valid @RequestBody UpdateMovieRequestDto dto) {
        return movieService.updateMovie(dto);
    }

    @GetMapping("/director/{director}")
    public List<MovieDto> findByDirectorOrderByReleaseYearDesc(@PathVariable String director) {
        return movieService.findByDirectorOrderByReleaseYearDesc(director);
    }
}
