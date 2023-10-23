package com.example.devopslabs.movie.mapper;

import com.example.devopslabs.movie.Movie;
import com.example.devopslabs.movie.dto.AddMovieRequestDto;
import com.example.devopslabs.movie.dto.MovieDto;
import com.example.devopslabs.movie.dto.UpdateMovieRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDto movieToMovieDto(Movie movie);

    @Mapping(target = "id", ignore = true)
    Movie addMovieRequestDtoToMovie(AddMovieRequestDto addMovieRequestDto);

    Movie updateMovieRequestDtoToMovie(UpdateMovieRequestDto updateMovieRequestDto, @MappingTarget Movie movie);
}
