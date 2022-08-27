package ru.matyuk.irregularVerbsBot.design;

public final class Messages {

    public static final String HELLO_MESSAGE = "Привет, %s :relaxed: Давай учится!";
    public static final String CHOOSE_GROUP_FOR_VIEW_MESSAGE = "Выбирете группу для просмотра";
    public static final String CHOOSE_GROUP_FOR_LEARNING_MESSAGE = "Выбирете группу для изучения";
    public static final String VERBS_IN_GROUP_MESSAGE = "Глаголы в группе:";
    public static final String MAIN_MENU_MESSAGE = "Главное меню";
    public static final String SELECTED_GROUP_MESSAGE = "Выбраны группы для изучения:\n";
    public static final String CONGRATULATION_MESSAGE = "Поздравляю, вы выучили все слова из группы!\nНажмите \"Закончить\" и выберете новую группу слов";
    public static final String WRITE_ANSWER_MESSAGE = "Напиши вторую и третью форму через пробел для:\n\n%s - %s";
    public static final String INVALID_RESPONSE_MESSAGE = "Неверный формат ответа";
    public static final String RIGHT_MESSAGE = "Верно!";
    public static final String NOT_RIGHT_MESSAGE = "Неверно!\nПравильный ответ:";
    public static final String GOOD_WORK_MESSAGE = "Хорошо сегодня потрудились!";
    public static final String INSTRUCTION_CREATE_GROUP_MESSAGE = "Введите через пробел все инфинитивы (первая форма), " +
            "которые хотите добавить в группу и отправте сообщение";

    public static final String RESULT_MESSAGE = "Результат:";
    public static final String SET_GROUP_NAME_MESSAGE = "Введите название группы";
    public static final String GROUP_DONE_MESSAGE = "Группа '%s' создана";
    public static final String ARE_YOU_SURE_DELETE_ALL_DATA_MESSAGE = "Вы уверены что хотите удалить все данные о себе?" +
            "\n После того как вы нажмете '" + ButtonInline.ALL_DELETE.getText() + "' то ваш пользователь удалится, " +
            "удаляться все созданные группы и сброситься весь прогресс по обучению.";
    public static final String DELETE_GROUP_DONE_MESSAGE = "Группа удалена";
    public static final String CHOOSE_GROUP_FOR_DELETE_MESSAGE = "УДАЛЕНИЕ ГРУППЫ\n\nВыберете группу";
    public static final String DELETE_ALL_DATA_MESSAGE = "Все данные удалены.\nЧто бы заново учить глаголы нажмите в меню на /start";
    public static final String TYPE_TEXT_FEEDBACK_MESSAGE = "Напишите свой вопрос/предложение";
    public static final String FEEDBACK_CREATED_MESSAGE = "Ваше обращение принято";
    public static final String SETTING_GROUP_MESSAGE = "Тут вы можете создать свою группу глаголов или удалить созданную";

    public static final String INSTRUCTION_LEARN_MESSAGE = "Тут вам будет показывать инфинитив и перевод.\n" +
            "Вам надо будет написать вторую и третью форму через пробел. " +
            "Если у какой-то формы более двух форм можно написать две формы через / или просто написать одну из них.\n" +
            "Например:\n" +
            "be - быть/находиться\n" +
            "Правильным ответом будет считать:\n" +
            "was been\nwas/were been\nwere been";

}
