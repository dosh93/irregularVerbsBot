package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.design.Smiles;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.UserGroupLearning;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.CHOOSE_GROUP_FOR_LEARNING_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.NO_SELECTED_GROUP_MESSAGE;

@Component
public class ChooseGroupProcessing extends MainProcessing {

    public ChooseGroupProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController, SessionController sessionController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController, sessionController);
    }

    public Response processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_MAIN.name())){
            return back(user, messageId);
        } else {
            return chooseGroup(user, messageId, callbackQuery.getData());
        }
    }

    @Override
    public Response processing(String messageText, User user) {
        return null;
    }

    private Response chooseGroup(User user, Integer messageId, String data) {
        Group group = groupController.getGroup(Long.parseLong(data));
        List<Verb> verbList = new ArrayList<>();
        group.getVerbs().forEach(groupVerb -> verbList.add(groupVerb.getVerb()));
        user = userController.saveLearning(user, verbList, group);

        ReplyKeyboard replyKeyboard = keyboard.getGroupChoseKeyboard(user);

        StringBuilder responseMessage = new StringBuilder(CHOOSE_GROUP_FOR_LEARNING_MESSAGE)
                .append(Messages.SELECTED_GROUP_MESSAGE);
        List<UserGroupLearning> groupLearnings = user.getGroupLearnings();
        if(groupLearnings.size() > 0){
            groupLearnings.forEach(groupLearning -> responseMessage.append(Smiles.MINUS).append(" ")
                    .append(groupLearning.getGroup().getName()).append("\n"));
        }else {
            responseMessage.append(NO_SELECTED_GROUP_MESSAGE);
        }

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(messageId, user.getChatId()))))
                .responseMessage(getResponseMessage(responseMessage.toString(), user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

}
