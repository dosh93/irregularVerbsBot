package ru.matyuk.irregularVerbsBot.precessing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.precessing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;

@Component
public class DeleteGroupProcessing extends MainProcessing{

    public DeleteGroupProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_SETTING_GROUP.name())){
            settingGroup(user, messageId);
        } else {
            deleteGroup(user, messageId, callbackQuery.getData());
        }
    }

    @Override
    public void processing(String messageText, User user) {

    }
    private void deleteGroup(User user, Integer messageId, String data) {
        user = userController.setState(user, StateUser.SETTING_GROUP_STATE);
        groupController.delete(Long.parseLong(data));

        ReplyKeyboard replyKeyboard = keyboard.getSettingGroupButton();
        String responseText = DELETE_GROUP_DONE_MESSAGE +
                "\n\n" + SETTING_GROUP_MESSAGE;

        ResponseMessage response = ResponseMessage.builder()
                .message(responseText)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();
        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }




}
