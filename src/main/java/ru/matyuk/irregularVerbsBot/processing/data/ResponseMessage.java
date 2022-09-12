package ru.matyuk.irregularVerbsBot.processing.data;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.File;

@Data
@Builder
public class ResponseMessage {

    private long chatId;
    private String message;
    private ReplyKeyboard keyboard;
    private File audio;
    private String audioName;
}
