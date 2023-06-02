package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new LinkedHashMap<>();
    private int countId = 0;

    @Override
    public User getUserById(int userId) throws NotFoundException {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return users.get(userId);
    }

    public User addUser(User user) {
        user.setId(genNewId());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) throws NotFoundException {

        if (users.get(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден!");
        }
        users.put(user.getId(), user);
        log.warn("Пользователь изменён: " + user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private int genNewId() {
        return ++countId;
    }


}
