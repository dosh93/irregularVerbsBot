package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

import static org.hibernate.id.PersistentIdentifierGenerator.TABLE;


@Entity(name = "compilation")
@Data
public class Compilation {

    @Id
    @GeneratedValue(generator = TABLE)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "compilation", fetch = FetchType.LAZY)
    private List<CompilationVerb> verbs;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
