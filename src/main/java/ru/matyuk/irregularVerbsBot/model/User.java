package ru.matyuk.irregularVerbsBot.model;


import lombok.Data;
import ru.matyuk.irregularVerbsBot.enums.StateUser;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "users")
@Data
public class User {

    @Id
    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    private Timestamp registerAt;

    @Column(columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private StateUser state = StateUser.REGISTERED_STATE;

    @OneToMany(mappedBy = "user",orphanRemoval = true,
            cascade = CascadeType.REMOVE)
    private List<Learning> learnings;

    @OneToMany(mappedBy = "user",orphanRemoval = true,
            cascade = CascadeType.REMOVE)
    private List<Compilation> compilations;

    @Lob
    @Basic(fetch=LAZY)
    private String tmp;

    @Column(columnDefinition = "int default 3")
    private int countSuccessful;

}
