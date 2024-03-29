package ru.matyuk.irregularVerbsBot.processing.data;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import ru.matyuk.irregularVerbsBot.model.User;

import java.util.List;

@Builder
@Data
public class Response {

    private User user;
    private ResponseMessage responseMessage;
    private List<DeleteMessage> deleteMessage;
    private boolean isSaveSentMessageId;

}
