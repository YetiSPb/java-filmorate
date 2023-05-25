package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new LinkedHashMap<>();
    private int countId = 0;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody  User user) {
        try {
            generalValidateUser(user);
            user.setId(genNewId());
            users.put(user.getId(), user);
            log.info("Добавлен пользователь: " + user);
        } catch (ValidationException e) {
            log.warn("Ошибка ввода данных: " + e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.warn("Пользователь добавлен: " + user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody  User user) throws ValidationException {

        try {

            if (users.get(user.getId()) == null) {
                throw new ValidationException("Пользователь не существует!");
            }

            generalValidateUser(user);
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: " + user);
        } catch (ValidationException e) {
            log.warn("Ошибка ввода данных: " + e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.warn("Пользователь изменён: " + user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
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
