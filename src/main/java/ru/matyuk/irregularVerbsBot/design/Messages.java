package ru.matyuk.irregularVerbsBot.design;

public final class Messages {

    public static final String HELLO_MESSAGE = "Привет, %s :relaxed: Давай учится! " + Smiles.BRAIN;
    public static final String CHOOSE_GROUP_FOR_VIEW_MESSAGE = "<b>Просмотр групп " + Smiles.BOOK +"</b>\n\n" +
     "Выбирете группу что бы посмотреть какие глаголы есть в группе " + Smiles.POINT_DOWN;
    public static final String CHOOSE_GROUP_FOR_LEARNING_MESSAGE = "<b>Выбор группы</b> " + Smiles.PUSH_PIN + "\n\n";
    public static final String VERBS_IN_GROUP_MESSAGE = "<b>Глаголы в группе:</b> " + Smiles.POINT_DOWN + "\n\n";
    public static final String MAIN_MENU_MESSAGE = "<b>Главное меню</b> " + Smiles.HOUSE + "\n\n" +
            Smiles.RADIO_BUTTON + "Нажмите '" + ButtonInline.LEARNING.getText() + "' что бы начать учить глаголы (кнопка доступна если выбрана хоть одна групп)\n\n" +
            Smiles.RADIO_BUTTON + "Нажмите '" + ButtonInline.VIEW_GROUP.getText() + "' что бы посмотреть группы глаголов\n\n" +
            Smiles.RADIO_BUTTON + "Нажмите '" + ButtonInline.CHOOSE_GROUP.getText() + "' что бы выбрать группы глаголов для изучения\n\n" +
            Smiles.RADIO_BUTTON + "Нажмите '" + ButtonInline.SETTING_GROUP.getText() + "' там вы сможете создать свою группу глаголов или удалить свои группы\n\n" +
            Smiles.RADIO_BUTTON + "Нажмите '" + ButtonInline.FEEDBACK.getText() + "' если у вас есть вопросы\\пожелания\n\n" ;
    public static final String SELECTED_GROUP_MESSAGE = "<b>Выбраны группы для изучения:</b>\n\n";
    public static final String NO_SELECTED_GROUP_MESSAGE = "<b>Не выбрано ни одной группы</b>";
    public static final String CONGRATULATION_MESSAGE = "<b>Поздравляю</b>, вы выучили все слова из группы! " + Smiles.CONFETTI + "\n\n" +
            "\nНажмите " + ButtonInline.END_LEARNING.getText() + " и выберете новую группу слов";
    public static final String WRITE_ANSWER_MESSAGE =  "<b>Учимся</b> " +Smiles.BRAIN + "\n\n" +
            "Напиши <b>вторую и третью форму через пробел</b>:\n\n%s - %s";
    public static final String INVALID_RESPONSE_MESSAGE = "Неверный формат ответа " + Smiles.DELETE;
    public static final String RIGHT_MESSAGE = "Верно! " + Smiles.WHITE_CHECK_MARK;
    public static final String NOT_RIGHT_MESSAGE = "Неверно! " + Smiles.DELETE + "\nПравильный ответ:";
    public static final String GOOD_WORK_MESSAGE = "Хорошо сегодня потрудились! " + Smiles.MUSCLE;
    public static final String INSTRUCTION_CREATE_GROUP_MESSAGE = "<b>Создание группы</b> " + Smiles.PENCIL + "\n\n" +
            "Введите через <b>пробел</b> все <b>инфинитивы (первая форма)</b>, " +
            "которые хотите добавить в группу и отправте сообщение";

    public static final String RESULT_MESSAGE = "<b>Создание группы</b> " + Smiles.PENCIL + "\n\n" +
            "Результат:\n";
    public static final String SET_GROUP_NAME_MESSAGE = "<b>Создание группы</b> " + Smiles.PENCIL + "\n\n" +
            "Введите название группы " + Smiles.POINT_DOWN;
    public static final String GROUP_DONE_MESSAGE = "Группа <b>'%s'</b> создана " + Smiles.WHITE_CHECK_MARK + "\n\n";
    public static final String ARE_YOU_SURE_DELETE_ALL_DATA_MESSAGE = "<b>Удаление данных</b> " + Smiles.SIGN_STOP + "\n\n" +
            "После того как вы нажмете '" + ButtonInline.ALL_DELETE.getText() + "' то удалится <b>ваш пользователь</b>, " +
            "<b>все созданные группы</b> и <b>сбросится весь прогресс по обучению</b>.\n\n" +
            "Вы уверены что хотите удалить все данные о себе?";
    public static final String DELETE_GROUP_DONE_MESSAGE = "Группа удалена " + Smiles.DELETE;
    public static final String CHOOSE_GROUP_FOR_DELETE_MESSAGE = "<b>Удаление группы</b> " + Smiles.DELETE + "\n\n" +
            "Выберете группу для удаления " + Smiles.POINT_DOWN;
    public static final String DELETE_ALL_DATA_MESSAGE = "<b>Все данные удалены</b>" + Smiles.DELETE + "\n\n" +
            "Что бы начать заново учить глаголы нажмите в меню на /start" + Smiles.POINT_RIGHT;
    public static final String TYPE_TEXT_FEEDBACK_MESSAGE = "<b>Обратная связь</b> " + Smiles.MESSAGE + "\n\n" +
            "Напишите свой вопрос/предложение";
    public static final String FEEDBACK_CREATED_MESSAGE = "Ваше обращение принято " + Smiles.WHITE_CHECK_MARK;
    public static final String SETTING_GROUP_MESSAGE = "<b>Настройка групп</b> " + Smiles.SETTING + "\n\n" +
            Smiles.RADIO_BUTTON + "Нажмите '" + ButtonInline.CREATE_GROUP.getText() + "' что бы создать группу со своим набором глаголов\n\n" +
            Smiles.RADIO_BUTTON + "Нажмите '" + ButtonInline.REMOVE_GROUP.getText() + "' что бы удалить группу";

    public static final String INSTRUCTION_LEARN_MESSAGE = "<b>Учимся</b> " +Smiles.BRAIN + "\n\n" +
            "Тут вам будет показывать инфинитив и перевод.\n" +
            "Вам надо будет написать <b>вторую и третью форму через пробел</b>\n" +
            "Если у какой-то формы более двух форм можно написать <b>две формы через / или просто написать одну из них</b>\n" +
            "<b>Например:</b>\n" +
            "be - быть/находиться\n" +
            "<b>Правильным ответом будет считать:</b>\n" +
            "was been\nwas/were been\nwere been";

}
