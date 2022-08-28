package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.*;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.matyuk.irregularVerbsBot.design.Messages.RESET_GROUP_DONE_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.SETTING_LEARNING_MESSAGE;

@Component
public class ChooseGroupResetProcessing extends MainProcessing{
    public ChooseGroupResetProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_SETTING_LEARNING.name())){
            settingLearning(user, messageId);
        } else {
            chooseGroupReset(user, messageId, callbackQuery.getData());
        }
    }

    private void chooseGroupReset(User user, Integer messageId, String data) {
        user = userController.setState(user, StateUser.SETTING_LEARNING_STATE);

        Group groupNeedReset = groupController.getGroup(Long.parseLong(data));
        List<Long> verbIdsNeedDelete = getVerbIdsNeedDelete(user, groupNeedReset);
        userGroupLearningController.delete(userGroupLearningController.getByUserAndGroup(user, groupNeedReset));
        learningController.delete(user, verbIdsNeedDelete);

        user = userController.getUser(user.getChatId());

        String responseMessage = SETTING_LEARNING_MESSAGE + String.format(RESET_GROUP_DONE_MESSAGE, groupNeedReset.getName());

        ReplyKeyboard replyKeyboard = keyboard.getSettingLearningButton();

        ResponseMessage response = ResponseMessage.builder()
                .message(responseMessage)
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

    private List<Long> getVerbIdsNeedDelete(User user, Group groupNeedReset){
        List<Long> needDeleteInLearning = groupNeedReset.getVerbs()
                .stream().map(GroupVerb::getVerb)
                .map(Verb::getId)
                .collect(Collectors.toList());
        Set<Verb> notNeedDelete = new HashSet<>();
        List<Group> notResetGroup = user.getGroupLearnings().stream()
                .map(UserGroupLearning::getGroup)
                .filter(group -> !group.getId().equals(groupNeedReset.getId()))
                .collect(Collectors.toList());
        notResetGroup.forEach(group -> notNeedDelete.addAll(group.getVerbs().stream()
                .map(GroupVerb::getVerb).collect(Collectors.toList())));
        List<Long> notResetVerbIds = notNeedDelete.stream().map(Verb::getId).collect(Collectors.toList());
        notResetVerbIds.forEach(needDeleteInLearning::remove);
        return needDeleteInLearning;
    }

    @Override
    public void processing(String messageText, User user) {

    }
}
