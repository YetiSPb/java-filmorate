package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.RedoCreationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new RedoCreationException("Movie already exists");
        }
        if (film.getId() == 0) {
            generateId();
            film.setId(id);
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmExistenceCheck(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        filmExistenceCheck(id);
        return films.get(id);
    }

    @Override
    public void checkFilmExists(int filmId) {
        getFilmById(filmId);
    }

    private void generateId() {
        id++;
    }

    private void filmExistenceCheck(int id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException(String.format("Film with id %s not found", id));
        }
    }
}
