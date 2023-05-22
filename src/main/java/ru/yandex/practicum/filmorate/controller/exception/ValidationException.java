package ru.yandex.practicum.filmorate.controller.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends Exception {

    public ValidationException(String msg) {
        super(msg);
        log.warn("Ошибка ввода данных: " + msg);
    }
}
