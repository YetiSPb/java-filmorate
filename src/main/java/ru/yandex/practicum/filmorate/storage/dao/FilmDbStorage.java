package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dal.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikesStorage likesStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FilmGenreLineStorage filmGenreLineStorage;

    @Override
    public Collection<Film> getFilms() {
        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        int filmId = simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue();


        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmGenreLineStorage.addGenres(film.getGenres(), filmId);
        }
        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ?, " +
                "MPA_ID = ? where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        Film oldFilm = getFilmById(film.getId());


        Collection<Genre> existingGenres = oldFilm.getGenres();
        if (!existingGenres.isEmpty()) {
            filmGenreLineStorage.deleteGenres(film.getId());
        }

        if (!film.getGenres().isEmpty()) {
            filmGenreLineStorage.addGenres(film.getGenres(), film.getId());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(int filmId) {
        Film film;
        String sqlQuery = "select * from FILMS where FILM_ID = ?";
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("Film with id %s not found", filmId));
        }
        return film;
    }

    @Override
    public void checkFilmExists(int filmId) {
        getFilmById(filmId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(mpaStorage.getMpaById(resultSet.getInt("mpa_id")))
                .likes(likesStorage.getListOfLikes(resultSet.getInt("film_id")))
                .genres(genreStorage.getListOfGenres(resultSet.getLong("film_id")))
                .build();
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate());
        values.put("MPA_id", film.getMpa().getId());
        return values;
    }
}
