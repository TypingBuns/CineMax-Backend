package com.example.CineMax.Service;

import com.example.CineMax.Entity.Movie;
import com.example.CineMax.Repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Movie createMovie(String movieJson, MultipartFile posterFile) throws IOException {
        Movie movie = objectMapper.readValue(movieJson, Movie.class);
        if (posterFile != null && !posterFile.isEmpty()) {
            movie.setPoster(posterFile.getBytes());
        }

        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(Long id, String movieJson, MultipartFile posterFile) throws IOException {

        Movie movieDetails = objectMapper.readValue(movieJson, Movie.class);
        Movie movie = movieRepository.findById(id).orElse(null);
            movie.setTitle(movieDetails.getTitle());
            movie.setDirector(movieDetails.getDirector());
            movie.setCategories(movieDetails.getCategories());
            movie.setDuration(movieDetails.getDuration());
            movie.setAgeRestriction(movieDetails.getAgeRestriction());
            movie.setLanguageVersion(movieDetails.getLanguageVersion());
            movie.setShortDescription(movieDetails.getShortDescription());
            movie.setTrailerLink(movieDetails.getTrailerLink());
            movie.setReleaseDate(movieDetails.getReleaseDate());

            if (posterFile != null && !posterFile.isEmpty()) {
                movie.setPoster(posterFile.getBytes());
            }

            return movieRepository.save(movie);
    }
}
