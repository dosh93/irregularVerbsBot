package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.GROUP_DONE_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.MAIN_MENU_MESSAGE;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.MAIN_MENU_STATE;

@Component
public class SetNameGroupProcessing extends MainProcessing{
    public SetNameGroupProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {

    }

    @Override
    public void processing(String messageText, User user) {
        user = userController.setState(user, MAIN_MENU_STATE);
        Group group = groupController.getGroup(user.getChatId().toString());
        groupController.setName(group, messageText);
        String responseText = String.format(GROUP_DONE_MESSAGE, messageText) + MAIN_MENU_MESSAGE;
        ReplyKeyboard replyKeyboard = keyboard.getMainMenu(user);
        ResponseMessage response = ResponseMessage.builder()
                .message(responseText)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();
        telegramBot.deleteMessage(Integer.parseInt(user.getTmp()), user.getChatId());
        telegramBot.sendMessage(response);
    }
}
