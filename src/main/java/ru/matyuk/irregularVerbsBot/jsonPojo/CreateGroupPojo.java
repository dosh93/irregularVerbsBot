package ru.matyuk.irregularVerbsBot.jsonPojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateGroupPojo {

    private Long idGroup;
    private List<Long> verbIds;
}
