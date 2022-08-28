package ru.matyuk.irregularVerbsBot.design;

import lombok.Getter;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;

@Getter
public enum ButtonInline {

    GROUP(Smiles.CARD_FILE_BOX +" Группы", ButtonCommand.NONE),
    VIEW_GROUP(Smiles.BOOK + " Посмотреть", ButtonCommand.VIEW_GROUP),
    CHOOSE_GROUP(Smiles.PUSH_PIN + " Выбрать", ButtonCommand.CHOOSE_GROUP),
    SETTING_GROUP(Smiles.SETTING + " Настройка групп", ButtonCommand.SETTING_GROUP),
    LEARNING(Smiles.BRAIN + " Учить", ButtonCommand.LEARNING),
    BACK(Smiles.BACK + " Назад", ButtonCommand.BACK_TO_MAIN),
    START_LEARN(Smiles.START + " Начать", ButtonCommand.START_LEARN),
    CREATE_GROUP( Smiles.PENCIL + " Создать", ButtonCommand.CREATE_GROUP),
    REMOVE_GROUP( Smiles.DELETE + " Удалить", ButtonCommand.REMOVE_GROUP),
    BACK_TO_VIEW_GROUP( Smiles.BACK + " Назад", ButtonCommand.BACK_TO_VIEW_GROUP),
    BACK_TO_SETTING_GROUP( Smiles.BACK + " Назад", ButtonCommand.BACK_TO_SETTING_GROUP),
    CANCEL_CONFIRM(Smiles.STOP + " Отмена", ButtonCommand.CANCEL_CONFIRM),
    SAVE_GROUP( Smiles.SAVE + " Сохранить", ButtonCommand.SAVE_GROUP),
    END_LEARNING(Smiles.END + " Закончить", ButtonCommand.END_LEARNING),
    ALL_DELETE( Smiles.DELETE + " Удалить все данные!", ButtonCommand.ALL_DELETE),
    FEEDBACK( Smiles.MESSAGE + " Обратная связь", ButtonCommand.FEEDBACK);



    private String text;
    private ButtonCommand command;

    ButtonInline(String text, ButtonCommand command) {
        this.text = text;
        this.command = command;
    }
}
