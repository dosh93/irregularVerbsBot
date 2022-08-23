package ru.matyuk.irregularVerbsBot.jsonPojo;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.matyuk.irregularVerbsBot.enums.Command;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@Slf4j
public class CallbackQueryPojo {

    private Command command;
    private String data;

    public static CallbackQueryPojo getCallbackQueryPojo(String message){
        List<String> messageSplit = Arrays.asList(message.split(":"));
        if(messageSplit.size() != 2) return null;
        return CallbackQueryPojo.builder()
                .command(Command.fromString(messageSplit.get(0)))
                .data(messageSplit.get(1)).build();

    }
}
