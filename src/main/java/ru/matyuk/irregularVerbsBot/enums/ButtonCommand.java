package ru.matyuk.irregularVerbsBot.enums;

import lombok.Getter;

@Getter
public enum ButtonCommand {

    NONE,
    VIEW_GROUP,
    CHOOSE_GROUP,
    SETTING_GROUP,
    LEARNING,
    BACK_TO_MAIN,
    START_LEARN,
    CREATE_GROUP,
    REMOVE_GROUP,
    BACK_TO_VIEW_GROUP,
    BACK_TO_SETTING_GROUP,
    CANCEL_CONFIRM,
    SAVE_GROUP,
    END_LEARNING,
    ALL_DELETE,
    FEEDBACK;
}