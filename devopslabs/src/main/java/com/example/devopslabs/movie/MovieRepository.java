package com.example.devopslabs.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByDirectorOrderByReleaseYearDesc(String director);
    Boolean existsByTitle(String title);
}
