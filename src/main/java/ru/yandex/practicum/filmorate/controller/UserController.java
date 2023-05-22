package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new LinkedHashMap<>();
    private int countId = 0;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody @NotNull User user) {
        try {
            generalValidateUser(user);
            user.setId(genNewId());
            users.put(user.getId(), user);
            log.info("Добавлен пользователь: " + user);

        } catch (ValidationException e) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @NotNull User user) throws ValidationException {
        if (users.get(user.getId()) == null) {
            throw new ValidationException("Пользователь не существует!");
        }

        try {
            generalValidateUser(user);
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: " + user);

        } catch (ValidationException e) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
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

        Pattern patternEMail = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$");

        if (!patternEMail.matcher(user.getEmail()).matches()) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin().equals("") || user.getLogin().indexOf(" ") > 0 || user.getLogin() == null)
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.!");
        }

    }

}
