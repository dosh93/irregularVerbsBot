package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "groups_verb")
@Data
public class GroupVerb {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    private Set<Verb> verbs;
}
