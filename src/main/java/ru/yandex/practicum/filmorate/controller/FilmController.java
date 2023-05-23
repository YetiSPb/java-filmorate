package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int countId = 0;

    @PostMapping()
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {

        try {
            generalValidateFilm(film);
            film.setId(genNewId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм: " + film);

        } catch (ValidationException e) {
            log.warn("Ошибка ввода данных: " + e.getMessage());
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.warn("Фильм добавлен: " + film);
        return new ResponseEntity<>(film, HttpStatus.OK);

    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {

        try {
            if (films.get(film.getId()) == null) {
                throw new ValidationException("Фильм не найден!");
            }
            generalValidateFilm(film);
            films.put(film.getId(), film);
            log.info("Обновлен фильм: " + film);

        } catch (ValidationException e) {
            log.warn("Ошибка ввода данных: " + e.getMessage());
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.warn("Фильм изменён: " + film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private int genNewId() {
        return ++countId;
    }

    private void generalValidateFilm(@NotNull Film film) throws ValidationException {

        if (StringUtils.isEmpty(film.getName()))
            throw new ValidationException("Название не может быть пустым!");

        if (film.getDescription().length() > 200)
            throw new ValidationException("Максимальная длина описания — 200 символов!");

        if (film.getReleaseDate().isBefore(FilmController.MIN_DATE_RELEASE)) {
            throw new ValidationException("Дата релиза — не раньше 28.12.1985 !");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

    }

}
