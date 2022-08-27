package ru.matyuk.irregularVerbsBot.precessing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.jsonPojo.CreateGroupPojo;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.precessing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.matyuk.irregularVerbsBot.design.Messages.RESULT_MESSAGE;

@Component
public class CreateGroupProcessing extends MainProcessing{

    public CreateGroupProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }

    @Override
    public void processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_SETTING_GROUP.name())){
            settingGroup(user, messageId);
        }
    }

    public void processing(String messageText, User user) {
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

        StringBuilder responseText = new StringBuilder(RESULT_MESSAGE).append("\n");
        for (Map.Entry<String, Verb> one : verbsInfinitiveHashMap.entrySet()) {
            responseText.append(one.getValue() == null ? "❌ " : "✔️ ").append(one.getKey()).append("\n");
        }

        ReplyKeyboard replyKeyboard = keyboard.getConfirmCreateGroupButton();

        ResponseMessage response = ResponseMessage.builder()
                .message(responseText.toString())
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(oldMessage, user.getChatId());
        telegramBot.sendMessage(response);
    }

}
