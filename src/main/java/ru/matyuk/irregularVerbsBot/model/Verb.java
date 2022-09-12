package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "tg_verbs")
@Data
public class Verb {

    @Id
    @GeneratedValue
    private Long id;

    private String firstForm;

    private String secondForm;

    private String thirdForm;

    private String translate;

    @OneToMany(mappedBy = "verb", fetch = FetchType.LAZY)
    private List<GroupVerb> groups;

    @OneToMany(mappedBy = "verb",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private List<Learning> learnings;

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", firstForm, secondForm, thirdForm, translate);
    }

}
