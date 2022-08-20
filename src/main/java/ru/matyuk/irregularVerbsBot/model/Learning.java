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

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Verb verb;

    @Column(columnDefinition = "int default 0")
    private int countSuccessful;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean state;
}
