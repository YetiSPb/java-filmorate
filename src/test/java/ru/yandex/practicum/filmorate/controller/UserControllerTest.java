package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;

    @Test
    void addUserTest() throws ValidationException {

        User user = new User("francis@move.com", "Francis", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        userController.addUser(user);
        assertEquals(userController.getAllUsers().size(), 1);

        user = new User("", "Francis", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 1);


        user = new User("francismove.com", "", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 1);

        user = new User("francis@move.com", "Fra ncis", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 1);

        user = new User("francis@move.com", "Francis", "Francis Ford Coppola",
                LocalDate.of(2939, 4, 7));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 1);


        user = new User("francis@move.com", "Francis", "", LocalDate.of(1939, 4, 7));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 2);
        assertEquals(user.getLogin(), user.getName());
        assertTrue(userController.getAllUsers().contains(user));

    }

    @Test
    void updateUser() throws ValidationException {
        User user = new User("francis@move.com", "Coppola", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 3);

        User userForUpdate = new User(user.getId(), "francis@move.com", "Francis", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.updateUser(userForUpdate);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertTrue(userController.getAllUsers().contains(userForUpdate));

        user = new User(user.getId(), "", "Francis", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.updateUser(userForUpdate);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 3);

        user = new User(user.getId(), "francis@move.com", "", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.updateUser(userForUpdate);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 3);

        user = new User(user.getId(), "francis@move.com", " ", "Francis Ford Coppola",
                LocalDate.of(1939, 4, 7));
        try {
            userController.updateUser(userForUpdate);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 3);

        userForUpdate = new User(user.getId(), "francis@move.com", "Francis", "", LocalDate.of(1939, 4, 7));
        try {
            userController.updateUser(userForUpdate);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 3);
        assertEquals(userForUpdate.getLogin(), userForUpdate.getName());
        assertTrue(userController.getAllUsers().contains(userForUpdate));

        user = new User(user.getId(), "francis@move.com", "Francis", "Francis Ford Coppola",
                LocalDate.of(2939, 4, 7));
        try {
            userController.updateUser(userForUpdate);
        } catch (ValidationException e) {
            System.out.println("Тест ошибки" + e.getMessage());
        }
        assertEquals(userController.getAllUsers().size(), 3);
    }
}