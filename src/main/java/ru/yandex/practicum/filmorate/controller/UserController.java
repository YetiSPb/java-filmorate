package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        Logger.logRequest(HttpMethod.GET, "/users", "no body");
        return userService.getUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        Logger.logRequest(HttpMethod.POST, "/users", user.toString());
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        Logger.logRequest(HttpMethod.PUT, "/users", user.toString());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        Logger.logRequest(HttpMethod.GET, "/users/" + id, "no body");
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addAsFriend(@PathVariable long id,
                            @PathVariable long friendId) {
        Logger.logRequest(HttpMethod.PUT, "/users/" + id + "/friends/" + friendId, "no body");
        userService.addAsFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable long id,
                                  @PathVariable long friendId) {
        Logger.logRequest(HttpMethod.DELETE, "/users/" + id + "/friends/" + friendId, "no body");
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable long id) {
        Logger.logRequest(HttpMethod.GET, "/users/" + id + "/friends", "no body");
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getAListOfMutualFriends(@PathVariable long id,
                                              @PathVariable long otherId) {
        Logger.logRequest(HttpMethod.GET, "/users/" + id + "/friends/common/" + otherId, "no body");
        return userService.getAListOfMutualFriends(id, otherId);
    }
}
