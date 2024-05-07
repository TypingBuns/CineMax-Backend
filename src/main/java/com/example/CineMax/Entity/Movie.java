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

    @Column
    private String originalTitle;

    @Column
    private String categories;

    @Column
    private String country;

    @Column
    private Integer duration;

    @Column
    private Integer yearOfProduction;

    @Column(length = 10000)
    private String description;

    @Column
    private String trailerLink;

    @Column(name = "poster",length = 255)
    private String poster;

    @Column(name = "banner", length = 255)
    private String banner;

    @Column
    private Date releaseDate;
}
