package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {
    User getUserById(int userId);

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

/*    void addFriend(int idUser, int idFriend);

    List<User> getCommonFriends(int idUser, int idFriends);

    void deleteFriend(int idUser, int idFriend);

    List<User> getAllFriends(int idUser);*/
}
