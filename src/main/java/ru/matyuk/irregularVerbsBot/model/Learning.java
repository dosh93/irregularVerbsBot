package ru.matyuk.irregularVerbsBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "learnings")
@Data
public class Learning {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "verb_id", nullable = false)
    private Verb verb;

    @Column(columnDefinition = "int default 0")
    private int countSuccessful;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean state;
}
