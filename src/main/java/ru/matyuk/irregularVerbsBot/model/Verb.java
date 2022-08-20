package ru.matyuk.irregularVerbsBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

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
    private Set<GroupVerb> groups;

    @OneToMany(mappedBy = "verb", fetch = FetchType.LAZY)
    private Set<Learning> learnings;
}
