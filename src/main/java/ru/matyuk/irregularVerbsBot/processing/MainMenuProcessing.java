package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

@Component
public class MainMenuProcessing extends MainProcessing{


    public MainMenuProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    public void processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case LEARNING:
                learning(user, messageId);
                break;
            case SETTING_GROUP:
                settingGroup(user, messageId);
                break;
            case CHOOSE_GROUP:
                chooseGroup(user, messageId);
                break;
            case VIEW_GROUP:
                viewGroup(user, messageId);
                break;
            case FEEDBACK:
                feedback(user, messageId);
                break;
        }
    }


    @Override
    public void processing(String messageText, User user) {

    }

    private void learning(User user, Integer messageId) {
        user = userController.setState(user, StateUser.LEARNING_STATE);
        ReplyKeyboard replyKeyboard = keyboard.getStartLearningButton();

        ResponseMessage response = ResponseMessage.builder()
                .message(Messages.INSTRUCTION_LEARN_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    private void feedback(User user, Integer messageId) {
        user = userController.setState(user, StateUser.CREATE_FEEDBACK_STATE);

        ReplyKeyboard replyKeyboard = keyboard.getFeedbackButtons();

        ResponseMessage response = ResponseMessage.builder()
                .message(Messages.TYPE_TEXT_FEEDBACK_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        userController.setTmp(user, telegramBot.sendMessage(response).toString());

    }
}
