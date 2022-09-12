package ru.matyuk.irregularVerbsBot.design;

public final class Messages {

    public static final String HELLO_MESSAGE = "Привет, %s :relaxed: Давай учиться! " + Smiles.BRAIN;
    public static final String CHOOSE_GROUP_FOR_VIEW_MESSAGE = "<b>Просмотр групп " + Smiles.BOOK +"</b>\n\n" +
     "Выбери группу чтобы ознакомиться со списком глаголов " + Smiles.POINT_DOWN;
    public static final String CHOOSE_GROUP_FOR_LEARNING_MESSAGE = "<b>Выбор группы</b> " + Smiles.PUSH_PIN + "\n\n";
    public static final String VERBS_IN_GROUP_MESSAGE = "<b>Глаголы в группе:</b> " + Smiles.POINT_DOWN + "\n\n";
    public static final String MAIN_MENU_MESSAGE = "<b>Главное меню</b> " + Smiles.HOUSE + "\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.LEARNING.getSmile() + " чтобы начать учить глаголы (кнопка доступна, если выбрана хоть одна группа)\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.VIEW_GROUP.getSmile() + " чтобы ознакомиться с группами глаголов\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.CHOOSE_GROUP.getSmile() + " чтобы выбрать группы глаголов для изучения\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.SETTING_LEARNING.getSmile() + " чтобы сбросить прогресс обучения\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.SETTING_GROUP.getSmile() + " чтобы создать/удалить группу глаголов, настроить обучение\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.FEEDBACK.getSmile() + " если у тебя есть вопросы\\пожелания\n\n" ;
    public static final String SELECTED_GROUP_MESSAGE = "<b>Список групп на изучении:</b>\n";
    public static final String NO_SELECTED_GROUP_MESSAGE = "<b>Не выбрано ни одной группы</b>";
    public static final String CONGRATULATION_MESSAGE = "<b>Поздравляю</b>, ты выучил все слова в группе! " + Smiles.CONFETTI + "\n\n" +
            "\nНажми " + ButtonInline.END_LEARNING.getSmile() + " и выбери новую группу глаголов";
    public static final String WRITE_ANSWER_MESSAGE =  "<b>Учимся</b> " +Smiles.BRAIN + "\n\n" +
            "Напиши <b>вторую и третью форму через пробел</b>:\n\n%s - %s";
    public static final String INVALID_RESPONSE_MESSAGE = "Неверный формат ответа " + Smiles.DELETE;
    public static final String RIGHT_MESSAGE = "Верно! " + Smiles.WHITE_CHECK_MARK;
    public static final String NOT_RIGHT_MESSAGE = "Неверно! " + Smiles.DELETE + "\nПравильный ответ:";
    public static final String GOOD_WORK_MESSAGE = "Отличная работа! " + Smiles.MUSCLE;
    public static final String INSTRUCTION_CREATE_GROUP_MESSAGE = "<b>Создание группы</b> " + Smiles.PENCIL + "\n\n" +
            "Введи через <b>пробел</b> все <b>инфинитивы</b>, " +
            "которые хочешь добавить в группу";

    public static final String RESULT_MESSAGE = "<b>Создание группы</b> " + Smiles.PENCIL + "\n\n" +
            "Результат:\n";
    public static final String SET_GROUP_NAME_MESSAGE = "<b>Создание группы</b> " + Smiles.PENCIL + "\n\n" +
            "Введи название группы " + Smiles.POINT_DOWN;
    public static final String GROUP_DONE_MESSAGE = "Группа <b>'%s'</b> создана " + Smiles.WHITE_CHECK_MARK + "\n\n";
    public static final String ARE_YOU_SURE_DELETE_ALL_DATA_MESSAGE = "<b>Удаление данных</b> " + Smiles.SIGN_STOP + "\n\n" +
            "После того как ты нажмешь " + ButtonInline.ALL_DELETE.getSmile() + " то удалится <b>твой профиль</b>, " +
            "<b>все созданные группы</b> и <b>сбросится весь прогресс</b>.\n\n" +
            "Ты уверен что хочешь удалить все данные?";
    public static final String DELETE_GROUP_DONE_MESSAGE = "Группа удалена " + Smiles.DELETE;
    public static final String CHOOSE_GROUP_FOR_DELETE_MESSAGE = "<b>Удаление группы</b> " + Smiles.DELETE + "\n\n" +
            "Выбери группу для удаления " + Smiles.POINT_DOWN;
    public static final String DELETE_ALL_DATA_MESSAGE = "<b>Все данные удалены</b>" + Smiles.DELETE + "\n\n" +
            "Чтобы начать заново учить глаголы нажми в меню на /start" + Smiles.POINT_RIGHT;
    public static final String TYPE_TEXT_FEEDBACK_MESSAGE = "<b>Обратная связь</b> " + Smiles.MESSAGE + "\n\n" +
            "Напиши свой вопрос/предложение";
    public static final String FEEDBACK_CREATED_MESSAGE = "Спасибо за обратную связь " + Smiles.WHITE_CHECK_MARK;
    public static final String SETTING_GROUP_MESSAGE = "<b>Настройка групп</b> " + Smiles.SETTING + "\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.CREATE_GROUP.getSmile() + " чтобы создать группу со своим набором глаголов\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.REMOVE_GROUP.getSmile() + " чтобы удалить группу\n\n";

    public static final String INSTRUCTION_LEARN_MESSAGE = "<b>Учимся</b> " + Smiles.BRAIN + "\n\n" +
            "Бот предлагает инфинитив, а тебе нужно написать вторую и третью форму через пробел.\n" +
            "Некоторые формы глаголов имеют два варианта написания в этом случае вводим один из вариантов или оба через /.\n\n" +
            "<b>Например:</b>\n" +
            "get - получить\n" +
            "<b>Правильный ответ:</b>\n" +
            "got got\ngot got/gotten\ngot gotten";
    public static final String SETTING_LEARNING_MESSAGE = "<b>Управление обучением</b> " + Smiles.JOYSTICK + "\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.RESET_LEARNING_ALL.getSmile() + " чтобы сбросить весь прогресс обучения\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.RESET_LEARNING_GROUP.getSmile() + " чтобы сбросить прогресс обучения по группе\n\n";

    public static final String RESET_LEARNING_ALL_MESSAGE = "Твой прогресс сброшен " + Smiles.RELOAD;
    public static final String RESET_LEARNING_FRO_GROUP_MESSAGE =  "<b>Управление обучением</b> " + Smiles.JOYSTICK + "\n\n" +
            "Выбери группу для сброса прогресса " + Smiles.POINT_DOWN;

    public static final String RESET_GROUP_DONE_MESSAGE = "Прогресс по группе '%s' сброшен " + Smiles.RELOAD_BY_GROUP;

    public static final String SETTING_MAIN_MESSAGE_FORMAT = "<b>Настройки</b> " + Smiles.SETTING + "\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.SETTING_GROUP.getSmile() + " чтобы создать/удалить группу глаголов\n\n" +
            Smiles.RADIO_BUTTON + "Нажми " + ButtonInline.SET_COUNT_SUCCESSFUL.getSmile() + " чтобы настроить количество правильных ответов (подряд). Сейчас установлено: %s\n\n";

    public static final String MORE_VARIANT_VERB_MESSAGE = "Есть еще вариант написания глагола:";
    public static final String TYPE_NUMERICAL_MESSAGE = "Введи число больше 0:";
    public static final String SET_DONE_COUNT_SUCCESSFUL_MESSAGE = Smiles.WHITE_CHECK_MARK + "Значение установлено";

}
