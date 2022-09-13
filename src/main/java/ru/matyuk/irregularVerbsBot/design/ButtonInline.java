package ru.matyuk.irregularVerbsBot.design;

import lombok.Getter;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;

@Getter
public enum ButtonInline {

    VIEW_GROUP(Smiles.BOOK + " Посмотреть", ButtonCommand.VIEW_GROUP, Smiles.BOOK),
    CHOOSE_GROUP(Smiles.PUSH_PIN + " Выбрать", ButtonCommand.CHOOSE_GROUP, Smiles.PUSH_PIN),
    SETTING_GROUP(Smiles.SAW + " Настройка групп", ButtonCommand.SETTING_GROUP, Smiles.SAW),
    LEARNING(Smiles.BRAIN + " Учить", ButtonCommand.LEARNING, Smiles.BRAIN),
    BACK(Smiles.BACK + " Назад", ButtonCommand.BACK_TO_MAIN, Smiles.BACK),
    START_LEARN(Smiles.START + " Начать", ButtonCommand.START_LEARN, Smiles.START),
    CREATE_GROUP( Smiles.PENCIL + " Создать", ButtonCommand.CREATE_GROUP, Smiles.PENCIL),
    REMOVE_GROUP( Smiles.DELETE + " Удалить", ButtonCommand.REMOVE_GROUP, Smiles.DELETE),
    BACK_TO_VIEW_GROUP( Smiles.BACK + " Назад", ButtonCommand.BACK_TO_VIEW_GROUP, Smiles.BACK),
    BACK_TO_SETTING_GROUP( Smiles.BACK + " Назад", ButtonCommand.BACK_TO_SETTING_GROUP, Smiles.BACK),
    BACK_TO_SETTING_LEARNING( Smiles.BACK + " Назад", ButtonCommand.BACK_TO_SETTING_LEARNING, Smiles.BACK),
    CANCEL_CONFIRM(Smiles.STOP + " Отмена", ButtonCommand.CANCEL_CONFIRM, Smiles.STOP),
    SAVE_GROUP( Smiles.SAVE + " Сохранить", ButtonCommand.SAVE_GROUP, Smiles.SAVE),
    END_LEARNING(Smiles.END + " Закончить", ButtonCommand.END_LEARNING, Smiles.END),
    ALL_DELETE( Smiles.DELETE + " Удалить все данные!", ButtonCommand.ALL_DELETE, Smiles.DELETE),
    FEEDBACK( Smiles.MESSAGE + " Обратная связь", ButtonCommand.FEEDBACK, Smiles.MESSAGE),
    SETTING_LEARNING( Smiles.JOYSTICK + " Управление обучением", ButtonCommand.SETTING_LEARNING, Smiles.JOYSTICK),
    RESET_LEARNING_ALL( Smiles.RELOAD + " Сбросить всё", ButtonCommand.RESET_LEARNING_ALL, Smiles.RELOAD),
    RESET_LEARNING_GROUP( Smiles.RELOAD_BY_GROUP + " Сбросить по группе", ButtonCommand.RESET_LEARNING_GROUP, Smiles.RELOAD_BY_GROUP),
    SETTING_MAIN( Smiles.SETTING + " Настройки", ButtonCommand.SETTING_MAIN, Smiles.SETTING),
    BACK_TO_SETTING_MAIN( Smiles.BACK + " Назад", ButtonCommand.BACK_TO_SETTING_MAIN, Smiles.BACK),
    SET_COUNT_SUCCESSFUL( Smiles.KEY + " Установить значение", ButtonCommand.SET_COUNT_SUCCESSFUL, Smiles.KEY),
    ON_AUDIO(Smiles.SOUND + " Включить аудио", ButtonCommand.SWITCH_AUDIO, Smiles.SOUND),
    OFF_AUDIO(Smiles.MUTE + " Выключить аудио", ButtonCommand.SWITCH_AUDIO, Smiles.MUTE),
    BACK_MAIN_HOME(Smiles.HOUSE + " Главное меню", ButtonCommand.BACK_TO_MAIN, Smiles.HOUSE);



    private String text;
    private ButtonCommand command;
    private String smile;

    ButtonInline(String text, ButtonCommand command, String smile) {
        this.text = text;
        this.command = command;
        this.smile = smile;
    }
}
