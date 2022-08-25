package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "tg_group_verb")
@Data
public class GroupVerb {

    @EmbeddedId
    private GroupVerbId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("verbId")
    @JoinColumn(name = "verb_id")
    Verb verb;
}
