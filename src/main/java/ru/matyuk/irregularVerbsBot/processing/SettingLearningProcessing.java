package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

@Component
public class SettingLearningProcessing extends MainProcessing{
    public SettingLearningProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case RESET_LEARNING_ALL:
                resetLearningAll(user, messageId);
                break;
            case RESET_LEARNING_GROUP:
                resetLearningGroup(user, messageId);
                break;
            case BACK_TO_MAIN:
                back(user, messageId);
                break;
        }

    }

    private void resetLearningGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CHOOSE_GROUP_RESET_STATE);

        String responseMessage = RESET_LEARNING_FRO_GROUP_MESSAGE;
        ReplyKeyboard replyKeyboard = keyboard.getGroupForReset(user);

        ResponseMessage response = ResponseMessage.builder()
                .message(responseMessage)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    private void resetLearningAll(User user, Integer messageId) {
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);

        learningController.delete(user.getLearnings());
        userGroupLearningController.delete(user.getGroupLearnings());

        user = userController.getUser(user.getChatId());

        String responseMessage = MAIN_MENU_MESSAGE + RESET_LEARNING_ALL_MESSAGE;
        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);

        ResponseMessage response = ResponseMessage.builder()
                .message(responseMessage)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    @Override
    public void processing(String messageText, User user) {

    }
}
