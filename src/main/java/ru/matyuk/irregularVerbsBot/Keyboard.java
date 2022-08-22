package ru.matyuk.irregularVerbsBot;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.matyuk.irregularVerbsBot.controller.GroupVerbController;
import ru.matyuk.irregularVerbsBot.enums.Command;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.jsonPojo.CallbackQueryPojo;
import ru.matyuk.irregularVerbsBot.model.Compilation;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.enums.Command.*;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.*;

@Component
public class Keyboard {

    @Autowired
    private GroupVerbController groupVerbController;

    private int lengthRow = 3;
    public ReplyKeyboardMarkup getReplyKeyboardMarkupByState(StateUser state, Long chatId){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = null;
        switch (state){
            case REGISTERED_STATE:
            case START_LEARN_STATE:
                if(state == START_LEARN_STATE){
                    row = new KeyboardRow();
                    row.add(LEARNING.getName());
                    keyboardRowList.add(row);
                }
                row = new KeyboardRow();
                row.add(Command.VIEW_GROUP.getName());
                row.add(Command.CHOOSE_GROUP.getName());
                row.add(Command.CREATE_GROUP.getName());
                keyboardRowList.add(row);
                break;
            case VIEW_GROUP_STATE:
            case CHOOSE_GROUP_STATE:
                List<Compilation> groups = groupVerbController.getGroupsWithVerbsByChatId(chatId);
                int offset = 0;
                row = new KeyboardRow();
                for (Compilation group: groups) {
                    if(offset == lengthRow){
                        keyboardRowList.add(row);
                        row = new KeyboardRow();
                    }
                    row.add(group.getName());
                    offset++;
                }
                keyboardRowList.add(row);
                row = new KeyboardRow();
                row.add(BACK.getName());
                keyboardRowList.add(row);
                break;
            case LEARNING_IN_PROCESS_STATE:
                row = new KeyboardRow();
                row.add(Command.END.getName());
                keyboardRowList.add(row);
                break;
            case CREATE_GROUP_STATE:
                row = new KeyboardRow();
                row.add(Command.CANCEL.getName());
                keyboardRowList.add(row);

        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkupByState(StateUser state, Long chatId, String data){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        switch (state){
            case CREATE_GROUP_STATE:
                String callbackSave = SAVE.getName() + ":" + data;
                String callbackCancel = Command.CANCEL.getName() + ":" + data;

                InlineKeyboardButton cancel = createButtonInline(Command.CANCEL.getName(), callbackCancel);
                InlineKeyboardButton save = createButtonInline(SAVE.getName(), callbackSave);

                List<InlineKeyboardButton> row = new ArrayList<>();

                row.add(cancel);
                row.add(save);
                rows.add(row);
                break;
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton createButtonInline(String text, String callbackData){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
}
