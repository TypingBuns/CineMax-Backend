package com.example.CineMax.Service;

import com.example.CineMax.Entity.Movie;
import com.example.CineMax.Repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.upload.dir:src/main/resources/MovieImages}")
    private String uploadDir;

    @Transactional
    public Movie createMovie(String movieJson, MultipartFile posterFile, MultipartFile bannerFile) throws IOException {
        Movie movie = objectMapper.readValue(movieJson, Movie.class);
        if (!posterFile.isEmpty()) {
            String posterPath = saveFile(posterFile, "posters");
            movie.setPoster(posterPath);
        }
        if (!bannerFile.isEmpty()) {
            String bannerPath = saveFile(bannerFile, "banners");
            movie.setBanner(bannerPath);
        }
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(Long id, String movieJson, MultipartFile posterFile, MultipartFile bannerFile) throws IOException {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            Movie movieDetails = objectMapper.readValue(movieJson, Movie.class);
            updateMovieDetails(movie, movieDetails);
            if (!posterFile.isEmpty()) {
                String posterPath = saveFile(posterFile, "posters");
                movie.setPoster(posterPath);
            }
            if (!bannerFile.isEmpty()) {
                String bannerPath = saveFile(bannerFile, "banners");
                movie.setBanner(bannerPath);
            }
            return movieRepository.save(movie);
        }
        return null;
    }

    public void loadImages(Movie movie) {
        try {
            if (movie.getPoster() != null) {
                Path posterPath = Paths.get(movie.getPoster());
                movie.setPosterImage(Files.readAllBytes(posterPath));
            }
            if (movie.getBanner() != null) {
                Path bannerPath = Paths.get(movie.getBanner());
                movie.setBannerImage(Files.readAllBytes(bannerPath));
            }
        } catch (IOException e) {
            // Obsługa błędów, np. logowanie
            e.printStackTrace();
        }
    }

    private String saveFile(MultipartFile file, String folderName) throws IOException {
        Path directoryPath = Paths.get(uploadDir, folderName);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        Path filePath = directoryPath.resolve(file.getOriginalFilename());
        file.transferTo(filePath);
        return filePath.toString();
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
