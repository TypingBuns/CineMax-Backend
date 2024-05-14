package com.example.CineMax.Controller;

import com.example.CineMax.Entity.Movie;
import com.example.CineMax.Repository.MovieRepository;
import com.example.CineMax.Service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
@Validated
public class MovieController {
    private final MovieRepository movieRepository;
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> findAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        movies.forEach(movieService::loadImages);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> findMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movieService.loadImages(movie);
                    return ResponseEntity.ok(movie);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

