package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collections;
import java.util.List;

import static java.util.Comparator.*;

@Service
public class FilmService {
    @Autowired
    FilmStorage filmStorage;

    public void addLike(int id, int userId) {
        filmStorage.getFilmById(id).addLike(userId);
    }

    public void deleteLike(int id, int userId) throws NotFoundException {
        if (userId < 1) {
            throw new NotFoundException("Ошибка userId < 1 :" + userId);
        }
        filmStorage.getFilmById(id).deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = filmStorage.getAllFilms();

        popularFilms.sort(comparingInt(o -> o.getUsersLikes().size()));

        Collections.reverse(popularFilms);

        if (count > popularFilms.size()) {
            count = popularFilms.size();
        }

        popularFilms = popularFilms.subList(0, count);

        return popularFilms;
    }


}
