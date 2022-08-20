package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Entity(name = "groups_verb")
@Data
public class GroupVerb {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    private List<Verb> verbs;
}
