package ru.matyuk.irregularVerbsBot.precessing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.jsonPojo.CreateGroupPojo;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.precessing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.MAIN_MENU_STATE;

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
        user = userController.setState(user, MAIN_MENU_STATE);
        Group group = groupController.getGroup(user.getChatId().toString());
        groupController.setName(group, messageText);
        String responseText = String.format(GROUP_DONE_MESSAGE, messageText) + "\n\n" + MAIN_MENU_MESSAGE;
        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);
        ResponseMessage response = ResponseMessage.builder()
                .message(responseText)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();
        telegramBot.deleteMessage(Integer.parseInt(user.getTmp()), user.getChatId());
        telegramBot.sendMessage(response);
    }

    private void saveGroup(User user, Integer messageId) {
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
