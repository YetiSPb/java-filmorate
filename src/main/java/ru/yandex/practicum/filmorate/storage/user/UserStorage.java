package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUserById(int userId);

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

}
