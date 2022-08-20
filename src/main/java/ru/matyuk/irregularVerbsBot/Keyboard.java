package ru.matyuk.irregularVerbsBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.matyuk.irregularVerbsBot.controller.GroupVerbController;
import ru.matyuk.irregularVerbsBot.enums.StateUser;
import ru.matyuk.irregularVerbsBot.model.GroupVerb;

import java.util.ArrayList;
import java.util.List;

import static ru.matyuk.irregularVerbsBot.enums.StateUser.START_LEARN;

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
                row = new KeyboardRow();
                row.add("Просмотр групп глаголов");
                row.add("Выбор группы глаголов");
                keyboardRowList.add(row);
                if(state == START_LEARN){
                    row = new KeyboardRow();
                    row.add("Учится");
                    keyboardRowList.add(row);
                }
                break;
            case VIEW_GROUP:
            case CHOOSE_GROUP:
                List<GroupVerb> groups = groupVerbController.getGroupsWithVerbs();
                int offset = 0;
                row = new KeyboardRow();
                for (GroupVerb group: groups) {
                    if(offset == lengthRow){
                        keyboardRowList.add(row);
                        row = new KeyboardRow();
                    }
                    row.add(group.getName());
                    offset++;
                }
                keyboardRowList.add(row);
                row = new KeyboardRow();
                row.add("Назад");
                keyboardRowList.add(row);
                break;
            case LEARNING_IN_PROCESS:
                row = new KeyboardRow();
                row.add("Закончить");
                keyboardRowList.add(row);
                break;

        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
}
