package ru.matyuk.irregularVerbsBot.design;

import lombok.Getter;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;

@Getter
public enum ButtonInline {

    GROUP("Группы", ButtonCommand.NONE),
    VIEW_GROUP("Посмотреть", ButtonCommand.VIEW_GROUP),
    CHOOSE_GROUP("Выбрать", ButtonCommand.CHOOSE_GROUP),
    SETTING_GROUP("Настройка групп", ButtonCommand.SETTING_GROUP),
    LEARNING("Учить глаголы", ButtonCommand.LEARNING),
    BACK("Назад", ButtonCommand.BACK_TO_MAIN),
    START_LEARN("Начать", ButtonCommand.START_LEARN),
    CREATE_GROUP("Создать", ButtonCommand.CREATE_GROUP),
    REMOVE_GROUP("Удалить", ButtonCommand.REMOVE_GROUP),
    BACK_TO_VIEW_GROUP("Назад", ButtonCommand.BACK_TO_VIEW_GROUP),
    BACK_TO_SETTING_GROUP("Назад", ButtonCommand.BACK_TO_SETTING_GROUP),
    CANCEL_CONFIRM("Отмена", ButtonCommand.CANCEL_CONFIRM),
    SAVE_GROUP("Сохранить", ButtonCommand.SAVE_GROUP),
    END_LEARNING("Закончить", ButtonCommand.END_LEARNING),
    ALL_DELETE("Удалить все данные!", ButtonCommand.ALL_DELETE),
    FEEDBACK("Обратная связь", ButtonCommand.FEEDBACK);



    private String text;
    private ButtonCommand command;

    ButtonInline(String text, ButtonCommand command) {
        this.text = text;
        this.command = command;
    }
}
