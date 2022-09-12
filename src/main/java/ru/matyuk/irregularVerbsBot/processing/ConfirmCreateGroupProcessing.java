package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.jsonPojo.CreateGroupPojo;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.*;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.*;

@Component
public class ConfirmCreateGroupProcessing extends MainProcessing {

    public ConfirmCreateGroupProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        ButtonCommand command = ButtonCommand.valueOf(callbackQuery.getData());
        Integer messageId = callbackQuery.getMessage().getMessageId();

        switch (command){
            case CANCEL_CONFIRM:
                groupController.delete(user.getChatId().toString());
                return createGroup(user, messageId);
            case SAVE_GROUP:
                return saveGroup(user, messageId);
        }
        return null;
    }

    public Response processing(String messageText, User user) {
        return null;
    }

    private Response saveGroup(User user, Integer messageId) {
        user = userController.setState(user, SET_NAME_GROUP_STATE);

        CreateGroupPojo createGroupPojo = gson.fromJson(user.getTmp(), CreateGroupPojo.class);
        Group group = groupController.getGroup(createGroupPojo.getIdGroup());
        List<Verb> verbsByIds = verbController.getVerbsByIds(createGroupPojo.getVerbIds());

        groupVerbController.saveVerbsInGroup(group, verbsByIds);

        return Response.builder()
                .isSaveSentMessageId(true)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(SET_GROUP_NAME_MESSAGE, user.getChatId(), null))
                .user(user)
                .build();
    }


}
