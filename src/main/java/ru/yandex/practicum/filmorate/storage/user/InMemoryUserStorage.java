package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

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

    public User addUser(User user) throws ValidationException {
        generalValidateUser(user);
        user.setId(genNewId());
        users.put(user.getId(), user);
        return user;
    }


    public User updateUser(User user) throws ValidationException {

        if (users.get(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден!");
        }
        generalValidateUser(user);
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

    private void generalValidateUser(@NotNull User user) throws ValidationException {

        if (StringUtils.isEmpty(user.getEmail())) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (StringUtils.isEmpty(user.getLogin()) || user.getLogin().indexOf(" ") > 0)
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");

        if (StringUtils.isEmpty(user.getName())) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.!");
        }

    }

}
