package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.controller.*;
import ru.matyuk.irregularVerbsBot.design.Keyboard;
import ru.matyuk.irregularVerbsBot.design.Smiles;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.jsonPojo.CreateGroupPojo;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.processing.data.Response;

import java.util.*;
import java.util.stream.Collectors;

import static ru.matyuk.irregularVerbsBot.design.Messages.RESULT_MESSAGE;

@Component
public class CreateGroupProcessing extends MainProcessing {

    public CreateGroupProcessing(Keyboard keyboard, UserController userController, GroupController groupController, LearningController learningController, VerbController verbController, GroupVerbController groupVerbController, FeedbackController feedbackController, UserGroupLearningController userGroupLearningController) {
        super(keyboard, userController, groupController, learningController, verbController, groupVerbController, feedbackController, userGroupLearningController);
    }

    @Override
    public Response processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_SETTING_GROUP.name())){
            return settingGroup(user, messageId);
        }

        return null;
    }

    @Override
    public Response processing(String messageText, User user) {
        user = userController.setState(user, StateUser.CONFIRM_VERBS_IN_GROUP_STATE);
        String[] verbsStr = messageText.split(" ");
        HashMap<String, Verb> verbsInfinitiveHashMap = new HashMap<>();
        for (String verbInfinitive : verbsStr) {
            verbsInfinitiveHashMap.put(verbInfinitive, verbController.getVerbByFirstForm(verbInfinitive));
        }
        Long idGroup = groupController.createGroup(user).getId();
        List<Long> verbIds = verbsInfinitiveHashMap.values().stream()
                .filter(Objects::nonNull)
                .map(Verb::getId)
                .collect(Collectors.toList());

        Integer oldMessage = Integer.parseInt(user.getTmp());

        String createGroupJson = gson.toJson(CreateGroupPojo.builder()
                .idGroup(idGroup).verbIds(verbIds).build());
        user = userController.setTmp(user, createGroupJson);

        StringBuilder responseText = new StringBuilder(RESULT_MESSAGE);
        for (Map.Entry<String, Verb> one : verbsInfinitiveHashMap.entrySet()) {
            responseText.append(one.getValue() == null ? Smiles.DELETE + " " : Smiles.WHITE_CHECK_MARK + "️ ")
                    .append(one.getKey()).append("\n");
        }

        ReplyKeyboard replyKeyboard = keyboard.getConfirmCreateGroupButton();

        return Response.builder()
                .isSaveSentMessageId(false)
                .deleteMessage(new ArrayList<>(List.of(getDeleteMessage(oldMessage, user.getChatId()))))
                .responseMessage(getResponseMessage(responseText.toString(), user.getChatId(), replyKeyboard))
                .user(user)
                .build();
    }

}
