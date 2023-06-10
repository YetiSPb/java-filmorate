package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Film {
    @NotBlank(message = "Movie title must not be empty")
    private final String name;
    private final List<Integer> likes;
    @Builder.Default
    private final List<Genre> genres = new ArrayList<>();
    @PositiveOrZero(message = "id must not be negative")
    private int id;
    @Size(max = 200, message = "Maximum description length - 200 characters")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Movie duration must be positive")
    private int duration;
    private int rate;
    @NotNull(message = "Mpa must not be null")
    private Mpa mpa;
}
