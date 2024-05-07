package com.example.CineMax;

import com.example.CineMax.Entity.Movie;
import com.example.CineMax.Repository.MovieRepository;
import com.example.CineMax.Service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovieTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie;
    private List<Movie> allMovies;

    private MultipartFile posterFile;
    private MultipartFile bannerFile;

    @BeforeEach
    public void setup() throws Exception {
        movie = new Movie(); // Setup movie object with proper data
        allMovies = Arrays.asList(movie);

        posterFile = new MockMultipartFile("poster", "test image.png", "image/png", Files.readAllBytes(Paths.get("src/test/resources/MovieImages/posters/test image.png")));
        bannerFile = new MockMultipartFile("banner", "test image.png", "image/png", Files.readAllBytes(Paths.get("src/test/resources/MovieImages/banners/test image.png")));
    }

    @Transactional
    @Test
    public void findAllMovies_ShouldReturnAllMovies() throws Exception {
        when(movieRepository.findAll()).thenReturn(allMovies);
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.*", hasSize(1)));
    }

    @Transactional
    @Test
    public void createMovie_ShouldCreateMovie() throws Exception {
        when(movieService.createMovie(anyString(), any(), any())).thenReturn(movie);
        mockMvc.perform(multipart("/api/movies")
                        .file((MockMultipartFile) posterFile)
                        .file((MockMultipartFile) bannerFile)
                        .param("movie", objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void findMovieById_ShouldReturnMovie() throws Exception {
        when(movieRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(movie));
        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void findMovieById_NotFound_ShouldReturn404() throws Exception {
        when(movieRepository.findById(any())).thenReturn(java.util.Optional.empty());
        mockMvc.perform(get("/api/movies/999"))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    public void updateMovie_ShouldUpdateMovie() throws Exception {
        when(movieService.updateMovie(anyLong(), anyString(), any(), any())).thenReturn(movie);
        mockMvc.perform(multipart("/api/movies")
                        .file("poster", posterFile.getBytes())
                        .file("banner", bannerFile.getBytes())
                        .param("movie", objectMapper.writeValueAsString(movie)))
                .andExpect(status().isOk());
    }

    @Transactional
    @Test
    public void deleteMovie_ShouldDeleteMovie() throws Exception {
        doNothing().when(movieRepository).deleteById(any());
        mockMvc.perform(delete("/api/movies/1"))
                .andExpect(status().isOk());
    }
}
