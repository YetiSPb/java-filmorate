package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    FilmService filmService;

    @PostMapping()
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        filmStorage.addFilm(film);
        log.warn("Фильм добавлен: " + film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        filmStorage.updateFilm(film);
        log.warn("Фильм изменён: " + film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAllFilms());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getFilm(@PathVariable int id) {
        Film film = filmStorage.getFilmById(id);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<List<User>> deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false) Integer count) {
        if (count == null) {
            count = 10;
        }
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> handlerValidationException(final ValidationException e) {
        log.warn("Ошибка ввода данных: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private Map<String, String> handlerNotFoundException(final NotFoundException e) {
        log.warn("Ошибка ввода данных: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

}
