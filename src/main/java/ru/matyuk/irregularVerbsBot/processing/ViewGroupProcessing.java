package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.design.Smiles;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.model.id.GroupVerbId;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewGroupProcessing extends MainProcessing {

    public ViewGroupProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
    }

    public Response processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_MAIN.name())){
            return back(user, messageId);
        } else if (callbackQuery.getData().equals(ButtonCommand.BACK_TO_VIEW_GROUP.name())) {
            return viewGroup(user, messageId);
        } else {
            return viewGroup(user, messageId, callbackQuery.getData());
        }
    }

    @Override
    public Response processing(String messageText, User user) {
        return null;
    }

    private Response viewGroup(User user, Integer messageId, String data) {
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

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(getDeleteMessage(messageId, user.getChatId()))
                .responseMessage(getResponseMessage(responseMessage.toString(), user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

}
