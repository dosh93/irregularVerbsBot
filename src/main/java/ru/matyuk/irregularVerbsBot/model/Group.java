package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

import static org.hibernate.id.PersistentIdentifierGenerator.TABLE;


@Entity(name = "tg_group")
@Data
public class Group {

    @Id
    @GeneratedValue(generator = TABLE)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<GroupVerb> verbs;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
