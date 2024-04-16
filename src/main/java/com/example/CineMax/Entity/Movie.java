package com.example.CineMax.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "movie")
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private byte[] poster;

    @Column(nullable = false)
    private String title;

    @Column
    private String director;

    @Column
    private String shortDescription;

    @Column
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Column
    private Integer duration;

    @Column
    private String languageVersion;

    @Column
    private String trailerLink;

    @Column
    private String categories;

    @Column
    private Integer ageRestriction;
}

