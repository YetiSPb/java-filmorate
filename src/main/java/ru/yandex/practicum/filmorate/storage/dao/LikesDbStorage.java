package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.dal.LikesStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLike(int filmId, int userId) {
        Likes likes = Likes.builder()
                .filmId(filmId)
                .userId(userId)
                .build();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("likes");
        return simpleJdbcInsert.execute(toMap(likes)) > 0;
    }

    @Override
    public boolean unlike(int filmId, int userId) {
        String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Integer> getListOfLikes(int filmId) {
        String sqlQuery = "select USER_ID from LIKES where FILM_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
    }

    @Override
    public List<Integer> getTheBestFilms(int count) {
        String sqlQuery = "select FILMS.FILM_ID " +
                "from FILMS " +
                "left outer join LIKES ON FILMS.FILM_ID = LIKES.FILM_ID " +
                "group by FILMS.FILM_id " +
                "order by count(distinct LIKES.USER_ID) desc " +
                "limit ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, count);
    }

    private Map<String, Object> toMap(Likes likes) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_Id", likes.getUserId());
        values.put("film_Id", likes.getFilmId());
        return values;
    }
}
