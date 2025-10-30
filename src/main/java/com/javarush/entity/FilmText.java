package com.javarush.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "film_text")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmText implements IEntity {
    @Id
    @Column(name = "film_id")
    private Long id;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne
    @MapsId
    @JoinColumn(name = "film_id", referencedColumnName = "film_id")
    private Film film;
}
