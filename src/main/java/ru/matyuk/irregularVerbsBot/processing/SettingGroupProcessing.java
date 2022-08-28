package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.CHOOSE_GROUP_FOR_DELETE_MESSAGE;


@Component
public class SettingGroupProcessing extends MainProcessing{

    public SettingGroupProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());

        switch (command){
            case CREATE_GROUP:
                createGroup(user, messageId);
                break;
            case REMOVE_GROUP:
                removeGroup(user, messageId);
                break;
            case BACK_TO_MAIN:
                back(user, messageId);
                break;
        }
    }

    @Override
    public void processing(String messageText, User user) {

    }

    private void removeGroup(User user, Integer messageId) {
        user = userController.setState(user, StateUser.DELETE_GROUP_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getGroupKeyboardForDelete(user);

        ResponseMessage response = ResponseMessage.builder()
                .message(CHOOSE_GROUP_FOR_DELETE_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

}
