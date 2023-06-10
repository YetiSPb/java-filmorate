package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dal.FilmGenreLineStorage;
import ru.yandex.practicum.filmorate.storage.dal.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> getGenres() {
        Collection<Genre> genreInStorage = genreStorage.getGenres();
        Logger.logSave(HttpMethod.GET, "/genres", genreInStorage.toString());
        return genreInStorage;
    }

    public Genre getGenreById(int id) {
        Genre genreInStorage = genreStorage.getGenreById(id);
        Logger.logSave(HttpMethod.GET, "/genres/" + id, genreInStorage.toString());
        return genreInStorage;
    }

}
