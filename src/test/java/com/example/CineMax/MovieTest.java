package com.example.CineMax;

import com.example.CineMax.Controller.MovieController;
import com.example.CineMax.Entity.Movie;
import com.example.CineMax.Repository.MovieRepository;
import com.example.CineMax.Service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovieTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MovieController(movieRepository, movieService)).build();
    }

    @Test
    public void findAllMovies_ShouldReturnAllMovies() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setReleaseDate(new Date());

        given(movieRepository.findAll()).willReturn(Arrays.asList(movie));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andDo(print());
    }

    @Test
    public void createMovie_ShouldCreateMovie() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setReleaseDate(new Date());

        MockMultipartFile jsonFile = new MockMultipartFile("movie", "", "application/json", objectMapper.writeValueAsBytes(movie));
        MockMultipartFile posterFile = new MockMultipartFile("poster", "poster.jpg", "image/jpeg", "<<binary data>>".getBytes());
        MockMultipartFile bannerFile = new MockMultipartFile("banner", "banner.jpg", "image/jpeg", "<<binary data>>".getBytes());

        when(movieService.createMovie(anyString(), any(), any())).thenReturn(movie);

        mockMvc.perform(multipart("/api/movies")
                        .file(jsonFile)
                        .file(posterFile)
                        .file(bannerFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andDo(print());
    }


    @Test
    public void findMovieById_ShouldReturnMovie() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        when(movieRepository.findById(1L)).thenReturn(java.util.Optional.of(movie));

        mockMvc.perform(get("/api/movies/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andDo(print());
    }

    @Test
    public void findMovieById_NotFound_ShouldReturn404() throws Exception {
        when(movieRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/movies/{id}", 1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void updateMovie_ShouldUpdateMovie() throws Exception {
        String movieJson = """
    {
      "title": "The Shawshank Redemption",
      "originalTitle": "The Shawshank Redemption",
      "categories": "Drama, Ligma",
      "country": "USA",
      "duration": 142,
      "yearOfProduction": 1994,
      "description": "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
      "trailerLink": "https://example-trailer-link.com",
      "releaseDate": "1994-10-14"
    }
    """;

        // Tworzenie plików jako MockMultipartFile
        MockMultipartFile jsonFile = new MockMultipartFile("movie", "", "application/json", movieJson.getBytes());
        MockMultipartFile posterFile = new MockMultipartFile("poster", "poster.jpg", "image/jpeg", "<<binary data>>".getBytes());
        MockMultipartFile bannerFile = new MockMultipartFile("banner", "banner.jpg", "image/jpeg", "<<binary data>>".getBytes());

        // Wykonanie żądania PUT z użyciem danych multipart/form-data
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/movies/{id}", 1)
                        .file(jsonFile)
                        .file(posterFile)
                        .file(bannerFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Shawshank Redemption"))
                .andDo(print());
    }

    @Test
    public void deleteMovie_ShouldDeleteMovie() throws Exception {
        doNothing().when(movieRepository).deleteById(1L);

        mockMvc.perform(delete("/api/movies/{id}", 1))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
