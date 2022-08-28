package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.jsonPojo.CreateGroupPojo;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.*;

@Component
public class ConfirmCreateGroupProcessing extends MainProcessing{

    public ConfirmCreateGroupProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case CANCEL_CONFIRM:
                groupController.delete(user.getChatId().toString());
                createGroup(user, messageId);
                break;
            case SAVE_GROUP:
                saveGroup(user, messageId);
                break;
        }
    }

    public void processing(String messageText, User user) {

    }

    private void saveGroup(User user, Integer messageId) {
        user = userController.setState(user, SET_NAME_GROUP_STATE);

        CreateGroupPojo createGroupPojo = gson.fromJson(user.getTmp(), CreateGroupPojo.class);
        groupVerbController.saveVerbsInGroup(createGroupPojo.getIdGroup(), createGroupPojo.getVerbIds());

        ResponseMessage response = ResponseMessage.builder()
                .message(SET_GROUP_NAME_MESSAGE)
                .chatId(user.getChatId())
                .keyboard(null).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        userController.setTmp(user, telegramBot.sendMessage(response).toString());
    }


}
