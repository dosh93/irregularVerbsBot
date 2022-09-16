package ru.matyuk.irregularVerbsBot.utils;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import ru.matyuk.irregularVerbsBot.model.User;

public class CommonUtils {

    public static int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static DeleteMessage getDeleteAudio(User user){
        String tmp = user.getTmp();
        if(user.getTmp() != null && user.getTmp().contains("audioId:")){
            return DeleteMessage.builder()
                    .messageId(Integer.parseInt(user.getTmp().replace("audioId:", "")))
                    .chatId(String.valueOf(user.getChatId()))
                    .build();
        }
        return null;
    }
}
