package ru.matyuk.irregularVerbsBot.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.matyuk.irregularVerbsBot.enums.StateUser;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity(name = "users")
@Data
public class User {

    @Id
    private Long chatId;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Learning> learnings;

    private String firstName;

    private String lastName;

    private String userName;

    private Timestamp registerAt;

    @Column(columnDefinition = "int default 0")
    @Enumerated(EnumType.ORDINAL)
    private StateUser state;

}
