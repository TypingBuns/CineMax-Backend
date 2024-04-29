package com.example.CineMax.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String originalTitle;

    @Column(nullable = true)
    private String categories;

    @Column(nullable = true)
    private String country;

    @Column(nullable = true)
    private Integer duration;

    @Column(nullable = true)
    private Integer yearOfProduction;

    @Column(nullable = true, length = 10000)
    private String description;

    @Column(nullable = true)
    private String trailerLink;

    @Column(name = "poster", nullable = true)
    private byte[] poster;

    @Column(nullable = true)
    private Date releaseDate;
}
