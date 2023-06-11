package ru.yandex.practicum.filmorate.storage.dal;

import java.util.List;

public interface FriendsStorage {
    boolean addAsFriend(int userId, int friendId);

    boolean removeFromFriends(int userId, int friendId);

    List<Integer> getListOfFriends(int userId);

    List<Integer> getAListOfMutualFriends(int userId, int otherId);

}
