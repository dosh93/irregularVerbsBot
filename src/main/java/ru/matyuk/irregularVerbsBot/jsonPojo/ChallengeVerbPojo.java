package ru.matyuk.irregularVerbsBot.jsonPojo;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
@Builder
public class ChallengeVerbPojo {

    private LinkedList<Long> verbIds;
}
