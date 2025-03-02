package com.strelive.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "streams")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private String streamKey;

    private String thumbnail;

    private String videoUrl;

    @Column(nullable = false)
    private Boolean isLive;

    private Date createdAt;

    private Date startedAt;

    private Date endedAt;

    private String totalTime;

    @ManyToOne
    @JoinColumn(name = "streamer_id", nullable = false)
    private User streamer;

    @ManyToMany
    @JoinTable(
        name = "stream_categories",
        joinColumns = @JoinColumn(name = "stream_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;
}
