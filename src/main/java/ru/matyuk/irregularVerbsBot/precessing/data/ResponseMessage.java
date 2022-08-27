package ru.matyuk.irregularVerbsBot.precessing.data;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Data
@Builder
public class ResponseMessage {

    private long chatId;
    private String message;
    private ReplyKeyboard keyboard;

}
