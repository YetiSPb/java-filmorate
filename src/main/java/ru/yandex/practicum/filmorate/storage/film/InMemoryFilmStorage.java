package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int countId = 0;

    public Film getFilmById(int idFilm) throws NotFoundException {
        if (!films.containsKey(idFilm)) {
            throw new NotFoundException("Фильм с id " + idFilm + " не найден");
        }

        return films.get(idFilm);
    }

    public Film addFilm(Film film) throws ValidationException {
        film.setId(genNewId());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) throws NotFoundException {
        if (films.get(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден!");
        }
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private int genNewId() {
        return ++countId;
    }

}
