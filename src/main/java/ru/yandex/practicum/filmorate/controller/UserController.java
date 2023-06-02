package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    public User addUser(@RequestBody User user) throws ValidationException {
        generalValidateUser(user);
        userService.addUser(user);
        log.warn("Пользователь добавлен: " + user);
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) throws ValidationException {
        generalValidateUser(user);
        userService.updateUser(user);
        log.info("Обновлен пользователь: " + user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userService.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable int id) throws NotFoundException {
        return userService.getUserById(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException, NotFoundException {
        userService.addFriend(id, friendId);
        userService.addFriend(friendId, id);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        return userService.getAllUserFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
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
