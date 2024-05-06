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
    public Movie createMovie(String movieJson, MultipartFile posterFile, MultipartFile bannerFile) throws IOException {
        Movie movie = objectMapper.readValue(movieJson, Movie.class);
        if (posterFile != null && !posterFile.isEmpty()) {
            movie.setPoster(posterFile.getBytes());
        }
        if (bannerFile != null && !bannerFile.isEmpty()) {
            movie.setBanner(bannerFile.getBytes());
        }
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(Long id, String movieJson, MultipartFile posterFile, MultipartFile bannerFile) throws IOException {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            Movie movieDetails = objectMapper.readValue(movieJson, Movie.class);
            updateMovieDetails(movie, movieDetails);
            if (posterFile != null && !posterFile.isEmpty()) {
                movie.setPoster(posterFile.getBytes());
            }
            if (bannerFile != null && !bannerFile.isEmpty()) {
                movie.setBanner(bannerFile.getBytes());
            }
            return movieRepository.save(movie);
        }
        return null;
    }

    private void updateMovieDetails(Movie existingMovie, Movie newDetails) {
        existingMovie.setTitle(newDetails.getTitle());
        existingMovie.setOriginalTitle(newDetails.getOriginalTitle());
        existingMovie.setCategories(newDetails.getCategories());
        existingMovie.setCountry(newDetails.getCountry());
        existingMovie.setDuration(newDetails.getDuration());
        existingMovie.setYearOfProduction(newDetails.getYearOfProduction());
        existingMovie.setDescription(newDetails.getDescription());
        existingMovie.setTrailerLink(newDetails.getTrailerLink());
        existingMovie.setReleaseDate(newDetails.getReleaseDate());
    }
}
