package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;
import ru.matyuk.irregularVerbsBot.model.id.UserVerbId;

import javax.persistence.*;

@Entity(name = "tg_learnings")
@Data
public class Learning {

    @EmbeddedId
    private UserVerbId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("verbId")
    private Verb verb;

    @Column(columnDefinition = "int default 0")
    private int countSuccessful;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean state;
}
