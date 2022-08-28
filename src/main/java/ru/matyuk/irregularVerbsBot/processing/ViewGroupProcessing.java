package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.design.Smiles;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.model.id.GroupVerbId;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewGroupProcessing extends MainProcessing{

    public ViewGroupProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    public void processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_MAIN.name())){
            back(user, messageId);
        } else if (callbackQuery.getData().equals(ButtonCommand.BACK_TO_VIEW_GROUP.name())) {
            viewGroup(user, messageId);
        } else {
            viewGroup(user, messageId, callbackQuery.getData());
        }
    }

    @Override
    public void processing(String messageText, User user) {

    }

    private void viewGroup(User user, Integer messageId, String data) {
        ReplyKeyboard replyKeyboard = keyboard.getBackToViewGroupButton();
        StringBuilder responseMessage = new StringBuilder(Messages.VERBS_IN_GROUP_MESSAGE);

        List<Long> verbIds = groupVerbController.getGroupVerbByIdGroup(Long.parseLong(data)).stream()
                .map(GroupVerb::getId)
                .map(GroupVerbId::getVerbId)
                .collect(Collectors.toList());

        List<Verb> verbsByIds = verbController.getVerbsByIds(verbIds);
        for (Verb verb : verbsByIds) {
            responseMessage.append(Smiles.MINUS).append(" ").append(verb.toString()).append("\n");
        }

        ResponseMessage response = ResponseMessage.builder()
                .message(responseMessage.toString())
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

}
