package ru.matyuk.irregularVerbsBot.design;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.matyuk.irregularVerbsBot.controller.GroupController;
import ru.matyuk.irregularVerbsBot.controller.UserController;
import ru.matyuk.irregularVerbsBot.model.Group;
import ru.matyuk.irregularVerbsBot.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class Keyboard {

    @Autowired
    private GroupController groupVerbController;

    @Autowired
    private UserController userController;

    private int lengthRow = 3;

    private InlineKeyboardButton createButtonInline(String text, String callbackData){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public InlineKeyboardMarkup getMainMenu(User user){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        if(userController.isLearning(user)){
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton learning = createButtonInline(
                    ButtonInline.LEARNING.getText(),
                    ButtonInline.LEARNING.getCommand().name());
            row.add(learning);
            rows.add(row);
        }

        InlineKeyboardButton group = createButtonInline(
                ButtonInline.GROUP.getText(),
                ButtonInline.GROUP.getCommand().name());
        InlineKeyboardButton groupView = createButtonInline(
                ButtonInline.VIEW_GROUP.getText(),
                ButtonInline.VIEW_GROUP.getCommand().name());
        InlineKeyboardButton groupChoose = createButtonInline(
                ButtonInline.CHOOSE_GROUP.getText(),
                ButtonInline.CHOOSE_GROUP.getCommand().name());
        InlineKeyboardButton groupSetting = createButtonInline(
                ButtonInline.SETTING_GROUP.getText(),
                ButtonInline.SETTING_GROUP.getCommand().name());
        InlineKeyboardButton feedback = createButtonInline(
                ButtonInline.FEEDBACK.getText(),
                ButtonInline.FEEDBACK.getCommand().name());

        row1.add(group);
        row2.add(groupView);
        row2.add(groupChoose);
        row3.add(groupSetting);
        row4.add(feedback);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getGroupKeyboardForDelete(User user) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<Group> groups = user.getGroups();

        int offset = 0;
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Group group: groups) {
            if(offset == lengthRow){
                rows.add(row);
                row = new ArrayList<>();
            }
            row.add(createButtonInline(group.getName(), group.getId().toString()));
            offset++;
        }
        rows.add(row);
        row = new ArrayList<>();

        row.add(createButtonInline(
                ButtonInline.BACK_TO_SETTING_GROUP.getText(),
                ButtonInline.BACK_TO_SETTING_GROUP.getCommand().toString()));

        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getStartLearningButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton start = createButtonInline(
                ButtonInline.START_LEARN.getText(),
                ButtonInline.START_LEARN.getCommand().name());
        InlineKeyboardButton back = createButtonInline(
                ButtonInline.BACK.getText(),
                ButtonInline.BACK.getCommand().name());

        row1.add(start);
        row2.add(back);

        rows.add(row1);
        rows.add(row2);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getSettingGroupButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();


        InlineKeyboardButton createGroup = createButtonInline(
                ButtonInline.CREATE_GROUP.getText(),
                ButtonInline.CREATE_GROUP.getCommand().name());
        InlineKeyboardButton removeGroup = createButtonInline(
                ButtonInline.REMOVE_GROUP.getText(),
                ButtonInline.REMOVE_GROUP.getCommand().name());
        InlineKeyboardButton back = createButtonInline(
                ButtonInline.BACK.getText(),
                ButtonInline.BACK.getCommand().name());

        row1.add(createGroup);
        row1.add(removeGroup);
        row2.add(back);

        rows.add(row1);
        rows.add(row2);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getBackToViewGroupButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton back = createButtonInline(
                ButtonInline.BACK_TO_VIEW_GROUP.getText(),
                ButtonInline.BACK_TO_VIEW_GROUP.getCommand().name());

        row1.add(back);
        rows.add(row1);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getCreateGroupButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton back = createButtonInline(
                ButtonInline.BACK_TO_SETTING_GROUP.getText(),
                ButtonInline.BACK_TO_SETTING_GROUP.getCommand().name());

        row1.add(back);
        rows.add(row1);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getConfirmCreateGroupButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton cancel = createButtonInline(
                ButtonInline.CANCEL_CONFIRM.getText(),
                ButtonInline.CANCEL_CONFIRM.getCommand().name());
        InlineKeyboardButton save = createButtonInline(
                ButtonInline.SAVE_GROUP.getText(),
                ButtonInline.SAVE_GROUP.getCommand().name());

        row1.add(cancel);
        row1.add(save);
        rows.add(row1);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getGroupChoseKeyboard(User user) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<Group> groups = groupVerbController.getGroupNotLearning(user);

        int offset = 0;
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Group group: groups) {
            if(offset == lengthRow){
                rows.add(row);
                row = new ArrayList<>();
            }
            row.add(createButtonInline(group.getName(), group.getId().toString()));
            offset++;
        }
        rows.add(row);
        row = new ArrayList<>();

        row.add(createButtonInline(
                ButtonInline.BACK.getText(),
                ButtonInline.BACK.getCommand().toString()));
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getGroupViewKeyboard(User user) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<Group> groups = groupVerbController.getGroupsWithVerbsByChatId(user.getChatId());

        int offset = 0;
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Group group: groups) {
            if(offset == lengthRow){
                rows.add(row);
                row = new ArrayList<>();
            }
            row.add(createButtonInline(group.getName(), group.getId().toString()));
            offset++;
        }
        rows.add(row);
        row = new ArrayList<>();

        row.add(createButtonInline(
                ButtonInline.BACK.getText(),
                ButtonInline.BACK.getCommand().toString()));
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getEndLearningButton(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        row.add(createButtonInline(
                ButtonInline.END_LEARNING.getText(),
                ButtonInline.END_LEARNING.getCommand().toString()));

        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getAllDeleteButton(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        row1.add(createButtonInline(
                ButtonInline.CANCEL_CONFIRM.getText(),
                ButtonInline.CANCEL_CONFIRM.getCommand().toString()));
        row2.add(createButtonInline(
                ButtonInline.ALL_DELETE.getText(),
                ButtonInline.ALL_DELETE.getCommand().toString()));

        rows.add(row1);
        rows.add(row2);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public ReplyKeyboard getFeedbackButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        row1.add(createButtonInline(
                ButtonInline.BACK.getText(),
                ButtonInline.BACK.getCommand().toString()));

        rows.add(row1);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
}
