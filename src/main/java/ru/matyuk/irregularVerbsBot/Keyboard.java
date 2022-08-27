package ru.matyuk.irregularVerbsBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.matyuk.irregularVerbsBot.controller.GroupController;
import ru.matyuk.irregularVerbsBot.enums.Command;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.Group;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.enums.Command.*;

@Component
public class Keyboard {

    @Autowired
    private GroupController groupVerbController;

    private int lengthRow = 3;
    public ReplyKeyboardMarkup getReplyKeyboardMarkupByState(StateUser state, Long chatId){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row;
        switch (state){
            case START_LEARN_STATE:
                row = new KeyboardRow();
                row.add(LEARNING.getName());
                keyboardRowList.add(row);
            case REGISTERED_STATE:
                row = new KeyboardRow();
                row.add(Command.VIEW_GROUP.getName());
                row.add(Command.CHOOSE_GROUP.getName());
                keyboardRowList.add(row);
                row = new KeyboardRow();
                row.add(Command.CREATE_GROUP.getName());
                row.add(Command.DELETE_GROUP.getName());
                keyboardRowList.add(row);
                break;
            case VIEW_GROUP_STATE:
            case CHOOSE_GROUP_STATE:
                List<Group> groups = groupVerbController.getGroupsWithVerbsByChatId(chatId);
                int offset = 0;
                row = new KeyboardRow();
                for (Group group: groups) {
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

    public InlineKeyboardMarkup getInlineButtonsGroups(List<Group> groups){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int count = 0;
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Group group :  groups) {
            count++;
            String callbackData = DELETE_GROUP.getName() + ":" + group.getId();
            row.add(createButtonInline(group.getName(), callbackData));
            if(count == 2) count = 0;
            if(count == 0){
                rows.add(row);
                row = new ArrayList<>();
            }
        }
        if(row.size() > 0) rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardButton createButtonInline(String text, String callbackData){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public InlineKeyboardMarkup getConfirmDeleteButton(String data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        String callbackDelete = CONFIRM_DELETE.getName() + ":" + data;
        String callbackCancel = Command.CANCEL.getName() + ":" + data;

        InlineKeyboardButton cancel = createButtonInline(Command.CANCEL.getName(), callbackCancel);
        InlineKeyboardButton delete = createButtonInline(CONFIRM_DELETE.getName(), callbackDelete);

        row.add(cancel);
        row.add(delete);
        rows.add(row);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getRemoveKeyboard() {
       return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
    }

    public InlineKeyboardMarkup getCancelInline(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        String callbackCancel = Command.CANCEL.getName() + ":" + "-";
        InlineKeyboardButton cancel = createButtonInline(Command.CANCEL.getName(), callbackCancel);
        row.add(cancel);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
