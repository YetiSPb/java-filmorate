package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, 12, 28);
    @Autowired
    FilmService filmService;

    @PostMapping()
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        generalValidateFilm(film);
        filmService.addFilm(film);
        log.warn("Фильм добавлен: " + film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        generalValidateFilm(film);
        filmService.updateFilm(film);
        log.warn("Фильм изменён: " + film);
        return film;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmService.getAllFilms());
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable int id) {
        Film film = filmService.getFilmById(id);
        return film;
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        if (userId < 1) {
            throw new NotFoundException("Ошибка userId < 1 :" + userId);
        }
        filmService.deleteLike(id, userId);
    }

    @GetMapping(value = "/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        List<Film> filmList = new ArrayList<>(filmService.getPopularFilms(count));
        return filmList;
    }

    private void generalValidateFilm(Film film) throws ValidationException {

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
