package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.FEEDBACK_CREATED_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.MAIN_MENU_MESSAGE;

@Component
public class CreateFeedbackProcessing extends MainProcessing{
    public CreateFeedbackProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case BACK_TO_MAIN:
                back(user, messageId);
                break;
        }
    }

    @Override
    public void processing(String messageText, User user) {
        user = userController.setState(user, StateUser.MAIN_MENU_STATE);
        feedbackController.create(user, messageText);

        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);
        String responseText = FEEDBACK_CREATED_MESSAGE + "\n\n" + MAIN_MENU_MESSAGE;

        ResponseMessage response = ResponseMessage.builder()
                .message(responseText)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();
        telegramBot.deleteMessage(Integer.parseInt(user.getTmp()), user.getChatId());
        telegramBot.sendMessage(response);
    }
}
