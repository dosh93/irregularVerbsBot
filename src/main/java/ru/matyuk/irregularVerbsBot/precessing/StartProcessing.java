package ru.matyuk.irregularVerbsBot.precessing;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.precessing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;


@Slf4j
@Component
public class StartProcessing extends MainProcessing{


    public StartProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {

    }

    @Override
    public void processing(String messageText, User user) {
        String responseText = String.format(HELLO_MESSAGE, user.getFirstName());
        long chatId = user.getChatId();

        ResponseMessage response = ResponseMessage.builder()
                .message(responseText)
                .chatId(chatId)
                .keyboard(keyboard.getMainMenu(user))
                .build();
        telegramBot.sendMessage(response);
    }
}
