package ru.yandex.practicum.filmorate.storage.dal;

import java.util.List;

public interface LikesStorage {
    boolean addLike(int filmId, int userId);

    boolean unlike(int filmId, int userId);

    List<Integer> getListOfLikes(int filmId);

    List<Integer> getTheBestFilms(int count);

}
