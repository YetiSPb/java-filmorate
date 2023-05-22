package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new LinkedHashMap<>();
    private int countId = 0;

    @PostMapping()
    public ResponseEntity<Film> addFilm(@RequestBody @NotNull Film film)  {

        try {
            generalValidateFilm(film);
            film.setId(genNewId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм: " + film);

        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(film);
        }
        return ResponseEntity.ok(film);
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@RequestBody @NotNull Film film) throws ValidationException {

        if (films.get(film.getId()) == null) {
            throw new ValidationException("Фильм не найден!");
        }

        try {
            generalValidateFilm(film);
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Обновлен фильм: " + film);

        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(film);
        }
        return ResponseEntity.ok(film);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return films.values().stream().toList();
    }

    private int genNewId() {
        return ++countId;
    }

    private void generalValidateFilm(@NotNull Film film) throws ValidationException {

        if (film.getName() == null || film.getName().equals(""))
            throw new ValidationException("Название не может быть пустым!");

        if (film.getDescription().length() > 200)
            throw new ValidationException("Максимальная длина описания — 200 символов!");

        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            throw new ValidationException("Дата релиза — не раньше 28.12.1985 !");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

    }

}
