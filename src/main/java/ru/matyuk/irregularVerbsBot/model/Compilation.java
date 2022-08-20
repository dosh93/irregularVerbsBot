package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Entity(name = "compilation")
@Data
public class Compilation {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "compilations", fetch = FetchType.LAZY)
    private List<Verb> verbs;
}
