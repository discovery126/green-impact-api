package com.github.discovery126.greenimpact.exception;

public interface ValidationConstants {
    String CITY_NAME_NOT_FOUND = "Город с таким названием не найден";
    String CITY_ALREADY_EXIST = "Город с таким названием уже существует";
    String CITY_ID_NOT_FOUND = "Город с таким id не найден";

    String OPEN_CAGE_API_NOT_FOUND = "OpenCageAPI ключ отсутствует";
    String OPEN_CAGE_CITY_NOT_FOUND = "Город с указанным названием не найден";
    String OPEN_CAGE_ADDRESS_NOT_FOUND = "Улица с указанным адрессом не найден";

    String PHOTO_NOT_UPLOAD = "Файлы не загружены";

    String REWARD_CATEGORY_NOT_FOUND = "Категория награды с id не найдена";
    String REWARD_ID_NOT_FOUND = "Награда с таким id не найдена";
    String REWARD_OUT_OF_STOCK = "Награды данного типа закончились";
    String REWARD_NOT_ENOUGH_POINTS = "Не хватает баллов для обмена";

    String ROLE_NAME_NOT_FOUND = "Роль с таким именем не найдена";
    String ROLE_ID_NOT_FOUND = "Роль с таким id не найдена";

    String TASK_COMMENT_NOT_VALID = "Комментарий не может быть длиннее 255 символов";
    String TASK_ALREADY_ANSWERED = "Это задание уже проверенно";
    String TASK_ALREADY_TAKEN = "Такое задание уже взято";
    String TASK_ALREADY_TAKEN_TODAY = "Такое задание уже взято сегодня";
    String TASK_ALREADY_COMPLETED = "Такое задание уже выполнено";
    String TASK_ALREADY_COMPLETED_TODAY = "Такое задание уже выполнено сегодня";
    String TASK_ALREADY_TAKEN_FOR_CHECK = "Другой админ занимается этой проверкой";
    String TASK_CATEGORY_NOT_FOUND = "Категория задания с таким id не найдена";
    String TASK_ID_NOT_FOUND = "Задание с таким id не найдено";
    String TASK_COMPLETED_NOT_FOUND = "Выполненное задание с id не найдено";

    String USER_UNAUTHORIZED = "Пользователь не авторизован";
    String PASSWORD_CANT_BE_MORE_THAN_72 = "Пароль не может быть таким большим";

    String EVENT_ID_NOT_FOUND = "Мероприятие с таким id не найдено";
    String EVENT_NOT_ACTIVE = "Это мероприятие ещё не наступило или уже прошло";
    String EVENT_USER_ALREADY_CONFIRM = "Пользователь уже подтвердил участие в мероприятие";
    String EVENT_USER_ALREADY_REGISTERED = "Пользователь уже зарегистрирован";
    String EVENT_BAD_CODE = "Код неправильный";

    String EMAIL_ALREADY_EXIST = "Пользователь с такой email уже зарегистрирован";
    String DISPLAY_NAME_ALREADY_EXIST = "Пользователь с такой именем уже зарегистрирован";
    String USER_NOT_FOUND = "Пользователь не найден";
    String ADMIN_NOT_FOUND = "Админ не найден";
    String BAD_CREDENTIALS = "Неправильный логин или пароль";
}
