package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.ARE_YOU_SURE_DELETE_ALL_DATA_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.DELETE_ALL_DATA_MESSAGE;

@Component
public class AllDeleteProcessing extends MainProcessing{
    public AllDeleteProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case ALL_DELETE:
                allDelete(user, messageId);
                break;
            case CANCEL_CONFIRM:
                back(user, messageId);
                break;
        }

    }

    @Override
    public void processing(String messageText, User user) {
        user = userController.setState(user, StateUser.ALL_DELETE_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getAllDeleteButton();
        ResponseMessage response = ResponseMessage.builder()
                .message(ARE_YOU_SURE_DELETE_ALL_DATA_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.sendMessage(response);
    }

    private void allDelete(User user, Integer messageId) {
        userController.delete(user);
        ResponseMessage response = ResponseMessage.builder()
                .message(DELETE_ALL_DATA_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(null).build();
        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }
}
