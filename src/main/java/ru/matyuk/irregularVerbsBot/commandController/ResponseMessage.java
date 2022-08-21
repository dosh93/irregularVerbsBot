package ru.matyuk.irregularVerbsBot.commandController;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Data
@Builder
public class ResponseMessage {

    private long chatId;
    private String message;
    private ReplyKeyboard keyboard;

}
