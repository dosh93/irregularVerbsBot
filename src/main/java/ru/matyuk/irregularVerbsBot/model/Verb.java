package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "verbs")
@Data
public class Verb {

    @Id
    @GeneratedValue
    private Long id;

    private String firstForm;

    private String secondForm;

    private String thirdForm;

    private String translate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "verb_group",
            joinColumns = @JoinColumn(name = "verb_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<GroupVerb> groups;

    @OneToMany(mappedBy = "verb", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Learning> learnings;

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", firstForm, secondForm, thirdForm, translate);
    }
}
