package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserStorage userStorage;
    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userStorage.addUser(user);
        log.warn("Пользователь добавлен: " + user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) throws ValidationException {
        userStorage.updateUser(user);
        log.info("Обновлен пользователь: " + user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userStorage.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable int id, @PathVariable int friendId) throws ValidationException, NotFoundException {
        userService.addFriend(id, friendId);
        userService.addFriend(friendId, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping(value = "/{id}/friends")
    public ResponseEntity<List<User>> getAllFriends(@PathVariable int id) {
        List<User> userFriends = userService.getAllUserFriends(id);
        return new ResponseEntity<>(userFriends, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        List<User> CommonFriends = userService.getCommonFriends(id, otherId);
        return new ResponseEntity<>(CommonFriends, HttpStatus.OK);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> handlerValidationException(final ValidationException e) {
        log.warn("Ошибка ввода данных: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private Map<String, String> handlerNotFoundException(final NotFoundException e) {
        log.warn("Ошибка ввода данных: " + e.getMessage());
        return Map.of("error", e.getMessage());
    }

}
