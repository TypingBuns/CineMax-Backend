package com.example.CineMax.Controller;
import com.example.CineMax.Entity.Movie;
import com.example.CineMax.Repository.MovieRepository;
import com.example.CineMax.Service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository movieRepository;
    private final MovieService movieService;

    @GetMapping
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Movie> createMovie(
            @RequestParam("movie") String movieJson,
            @RequestParam("poster") MultipartFile posterFile) throws IOException {
        Movie movie = movieService.createMovie(movieJson, posterFile);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> findMovieById(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if(movie != null){
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable Long id,
            @RequestParam("movie") String movieJson,
            @RequestPart("poster") MultipartFile posterFile) throws IOException {

        Movie movie = movieService.updateMovie(id, movieJson, posterFile);
        return ResponseEntity.ok(movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}


