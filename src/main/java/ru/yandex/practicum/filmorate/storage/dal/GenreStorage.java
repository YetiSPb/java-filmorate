package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    Collection<Genre> getGenres();

    Genre getGenreById(int genreId);

    List<Genre> getListOfGenres(long id);
}
