package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public void addFriend(int idUser, int idFriend) throws  NotFoundException {
        userStorage.getUserById(idUser).addFriend(userStorage.getUserById(idFriend).getId());
    }

    public void deleteFriend(int idUser, int idFriend) throws NotFoundException {
        userStorage.getUserById(idUser).deleteFriend(idFriend);
    }

    public List<User> getAllUserFriends(int idUser) throws NotFoundException {
        List<User> friendsList = new ArrayList<>();
        for (int idFriend : userStorage.getUserById(idUser).getFriends()) {
            friendsList.add(userStorage.getUserById(idFriend));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(int idUser, int idFriends) throws NotFoundException {
        List<User> userFriends = getAllUserFriends(idUser);
        List<User> friendFriends = getAllUserFriends(idFriends);

        List<User> commonFriends = new ArrayList<>();

        for (User user : userFriends) {
            if (friendFriends.contains(user)) {
                commonFriends.add(user);
            }
        }

        return commonFriends;
    }

}
