package ru.matyuk.irregularVerbsBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.matyuk.irregularVerbsBot.controller.GroupVerbController;
import ru.matyuk.irregularVerbsBot.enums.Command;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.Compilation;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.enums.Command.BACK;
import static ru.matyuk.irregularVerbsBot.enums.Command.LEARNING;
import static ru.matyuk.irregularVerbsBot.enums.StateUser.*;

@Component
public class Keyboard {

    @Autowired
    private GroupVerbController groupVerbController;

    private int lengthRow = 3;
    public ReplyKeyboardMarkup getReplyKeyboardMarkupByState(StateUser state){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = null;
        switch (state){
            case REGISTERED:
            case START_LEARN:
                if(state == START_LEARN){
                    row = new KeyboardRow();
                    row.add(LEARNING.getName());
                    keyboardRowList.add(row);
                }
                row = new KeyboardRow();
                row.add(Command.VIEW_GROUP.getName());
                row.add(Command.CHOOSE_GROUP.getName());
                keyboardRowList.add(row);
                break;
            case VIEW_GROUP:
            case CHOOSE_GROUP:
                List<Compilation> groups = groupVerbController.getGroupsWithVerbs();
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
            case LEARNING_IN_PROCESS:
                row = new KeyboardRow();
                row.add(Command.END.getName());
                keyboardRowList.add(row);
                break;

        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
