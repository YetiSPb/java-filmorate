package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int countId = 0;

    public Film getFilmById(int idFilm) throws NotFoundException {
        if (!films.containsKey(idFilm)) {
            throw new NotFoundException("Фильм с id " + idFilm + " не найден");
        }

        return films.get(idFilm);
    }

    public Film addFilm(Film film) throws ValidationException {
        generalValidateFilm(film);
        film.setId(genNewId());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (films.get(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден!");
        }
        generalValidateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private int genNewId() {
        return ++countId;
    }

    private void generalValidateFilm(Film film) throws ValidationException {

        if (StringUtils.isEmpty(film.getName()))
            throw new ValidationException("Название не может быть пустым!");

        if (film.getDescription().length() > 200)
            throw new ValidationException("Максимальная длина описания — 200 символов!");

        if (film.getReleaseDate().isBefore(InMemoryFilmStorage.MIN_DATE_RELEASE)) {
            throw new ValidationException("Дата релиза — не раньше 28.12.1985 !");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

    }

}
