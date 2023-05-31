package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    FilmController filmController ;

    @Test
    void addFilmTest() throws ValidationException {
        Film film = new Film("Rumble Fish", "directed by Francis Ford Coppola",
                LocalDate.of(1983, 10, 21), 100);
        filmController.addFilm(film);
        assertEquals(filmController.getAllFilms().size(), 1);

        film = new Film("", "directed by Francis Ford Coppola", LocalDate.of(1983, 10, 21), 100);
        try {
            filmController.addFilm(film);
        } catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 1);

        film = new Film("Rumble Fish", " ".repeat(201),
                LocalDate.of(1983, 10, 21), 100);
        try {
            filmController.addFilm(film);
        }catch (ValidationException e){
        }

        assertEquals(filmController.getAllFilms().size(), 1);

        film = new Film("Rumble Fish", "directed by Francis Ford Coppola",
                LocalDate.of(1895, 12, 27), 100);
        try {
            filmController.addFilm(film);
        }catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 1);

        film = new Film("Rumble Fish", "directed by Francis Ford Coppola",
                LocalDate.of(1983, 10, 21), 0);
        try {
            filmController.addFilm(film);
        }catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 1);

    }

    @Test
    void updateFilm() throws ValidationException {
        Film film = new Film("Rumble Fish", "",
                LocalDate.of(1983, 10, 21), 100);
        try {
            filmController.addFilm(film);
        }catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 2);

        Film filmForUpdate = new Film(film.getId(), "Rumble Fish", "directed by Francis Ford Coppola",
                LocalDate.of(1979, 5, 19), 202);
        try {
            filmController.updateFilm(filmForUpdate);
        }catch (ValidationException e){
        }

        assertTrue(filmController.getAllFilms().contains(filmForUpdate));

        film = new Film(film.getId(), "", "directed by Francis Ford Coppola",
                LocalDate.of(1983, 10, 21), 100);
        try {
            filmController.updateFilm(filmForUpdate);
        }catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 2);
        assertTrue(filmController.getAllFilms().contains(filmForUpdate));

        film = new Film(film.getId(), "Rumble Fish", " ".repeat(201),
                LocalDate.of(1983, 10, 21), 100);
        try {
            filmController.updateFilm(filmForUpdate);
        }catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 2);
        assertTrue(filmController.getAllFilms().contains(filmForUpdate));

        film = new Film(film.getId(), "Rumble Fish", "directed by Francis Ford Coppola",
                LocalDate.of(1895, 12, 27), 100);
        try {
            filmController.updateFilm(filmForUpdate);
        }catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 2);
        assertTrue(filmController.getAllFilms().contains(filmForUpdate));

        film = new Film(film.getId(), "Rumble Fish", "directed by Francis Ford Coppola",
                LocalDate.of(1983, 10, 21), 0);
        try {
            filmController.updateFilm(filmForUpdate);
        }catch (ValidationException e){
        }
        assertEquals(filmController.getAllFilms().size(), 2);
        assertTrue(filmController.getAllFilms().contains(filmForUpdate));
    }

}