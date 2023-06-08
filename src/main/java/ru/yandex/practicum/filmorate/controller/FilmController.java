package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        Logger.logRequest(HttpMethod.GET, "/films", "no body");
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        Logger.logRequest(HttpMethod.POST, "/films", film.toString());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Logger.logRequest(HttpMethod.PUT, "/films", film.toString());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        Logger.logRequest(HttpMethod.GET, "/films/" + id, "no body");
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id,
                        @PathVariable long userId) {
        Logger.logRequest(HttpMethod.PUT, "/films/" + id + "/like/" + userId, "no body");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlike(@PathVariable long id,
                       @PathVariable long userId) {
        Logger.logRequest(HttpMethod.DELETE, "/films/" + id + "/like/" + userId, "no body");
        filmService.unlike(id, userId);
    }

    @GetMapping("/{id}/likes")
    public List<Long> getListOfLikes(@PathVariable long id) {
        Logger.logRequest(HttpMethod.GET, "/films/" + id + "/likes", "no body");
        return filmService.getListOfLikes(id);
    }

    @GetMapping("/popular")
    public List<Film> getTheBestFilms(@RequestParam(defaultValue = "10") @Positive int count) {
        Logger.logRequest(HttpMethod.GET, "/films/popular?count=" + count, "no body");
        return filmService.getTheBestFilms(count);
    }
}
