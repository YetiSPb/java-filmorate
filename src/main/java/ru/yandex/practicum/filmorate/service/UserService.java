package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public Collection<User> getUsers() {
        Collection<User> usersInStorage = userStorage.getUsers();
        Logger.logSave(HttpMethod.GET, "/users", usersInStorage.toString());
        return usersInStorage;
    }

    public User addUser(User user) {
        checkValidation(user);
        User userInStorage = userStorage.addUser(user);
        Logger.logSave(HttpMethod.POST, "/users", userInStorage.toString());
        return userInStorage;
    }

    public User updateUser(User user) {
        checkValidation(user);
        User userInStorage = userStorage.updateUser(user);
        Logger.logSave(HttpMethod.PUT, "/users", userInStorage.toString());
        return userInStorage;
    }

    public User getUserById(int id) {
        User userInStorage = userStorage.getUserById(id);
        Logger.logSave(HttpMethod.GET, "/users/" + id, userInStorage.toString());
        return userInStorage;
    }

    public void addAsFriend(int id, int friendId) {
        boolean addition;
        userStorage.checkUserExists(id);
        userStorage.checkUserExists(friendId);
        addition = friendsStorage.addAsFriend(id, friendId);
        Logger.logSave(HttpMethod.PUT, "/users/" + id + "/friends/" + friendId, ((Boolean) addition).toString());
    }

    public void removeFromFriends(int id, int friendId) {
        boolean removal;
        userStorage.checkUserExists(id);
        userStorage.checkUserExists(friendId);
        removal = friendsStorage.removeFromFriends(id, friendId);
        if (!removal) {
            throw new ObjectNotFoundException(String.format("User with id %s is not friends with user with id %s",
                    id, friendId));
        }
        Logger.logSave(HttpMethod.DELETE, "/users/" + id + "/friends/" + friendId, ((Boolean) removal).toString());
    }

    public List<User> getListOfFriends(int id) {
        userStorage.checkUserExists(id);
        List<User> friendList = friendsStorage.getListOfFriends(id).stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        Logger.logSave(HttpMethod.GET, "/users/" + id + "/friends", friendList.toString());
        return friendList;
    }

    public List<User> getAListOfMutualFriends(int id, int otherId) {
        userStorage.checkUserExists(id);
        userStorage.checkUserExists(otherId);
        List<User> mutualFriends = friendsStorage.getAListOfMutualFriends(id, otherId).stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        Logger.logSave(HttpMethod.GET, "/users/" + id + "/friends/common/" + otherId, mutualFriends.toString());
        return mutualFriends;
    }

    private void checkValidation(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login must not contain spaces");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
