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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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

    @MockBean
    private MovieService movieService;

    @MockBean
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
        MockMultipartFile posterFile = new MockMultipartFile("poster", "file.txt", "text/plain", "some image".getBytes());

        when(movieService.createMovie(anyString(), any())).thenReturn(movie);

        mockMvc.perform(multipart("/api/movies")
                        .file(jsonFile)
                        .file(posterFile))
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
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        MockMultipartFile jsonFile = new MockMultipartFile("movie", "", "application/json", objectMapper.writeValueAsBytes(movie));
        MockMultipartFile posterFile = new MockMultipartFile("poster", "file.txt", "text/plain", "some image".getBytes());

        when(movieService.updateMovie(eq(1L), anyString(), any())).thenReturn(movie);

        mockMvc.perform(multipart("/api/movies/{id}", 1)
                        .file(jsonFile)
                        .file(posterFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
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
