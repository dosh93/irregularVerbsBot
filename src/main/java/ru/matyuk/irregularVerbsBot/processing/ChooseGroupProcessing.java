package ru.matyuk.irregularVerbsBot.processing;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.matyuk.irregularVerbsBot.design.Smiles;
import ru.matyuk.irregularVerbsBot.enums.ButtonCommand;
import ru.matyuk.irregularVerbsBot.design.Messages;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;
import ru.matyuk.irregularVerbsBot.model.UserGroupLearning;
import ru.matyuk.irregularVerbsBot.model.Verb;
import ru.matyuk.irregularVerbsBot.processing.data.ResponseMessage;
import ru.matyuk.irregularVerbsBot.service.TelegramBot;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.design.Messages.CHOOSE_GROUP_FOR_LEARNING_MESSAGE;
import static ru.matyuk.irregularVerbsBot.design.Messages.NO_SELECTED_GROUP_MESSAGE;

@Component
public class ChooseGroupProcessing extends MainProcessing {

    public ChooseGroupProcessing(TelegramBot telegramBot) {
        super(telegramBot);
    }


    public void processing(CallbackQuery callbackQuery, User user) {
        Integer messageId = callbackQuery.getMessage().getMessageId();

        if(callbackQuery.getData().equals(ButtonCommand.BACK_TO_MAIN.name())){
            back(user, messageId);
        } else {
            chooseGroup(user, messageId, callbackQuery.getData());
        }

    }

    @Override
    public void processing(String messageText, User user) {

    }

    private void chooseGroup(User user, Integer messageId, String data) {
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

        ResponseMessage response = ResponseMessage.builder()
                .message(responseMessage.toString())
                .chatId(user.getChatId())
                .keyboard(replyKeyboard).build();

        telegramBot.deleteMessage(messageId, user.getChatId());
        telegramBot.sendMessage(response);
    }

}
