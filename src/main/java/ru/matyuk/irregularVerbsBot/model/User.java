package ru.matyuk.irregularVerbsBot.model;


import lombok.Data;
import ru.matyuk.irregularVerbsBot.enums.StateUser;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "tg_users")
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
    private StateUser state = StateUser.MAIN_MENU_STATE;

    @OneToMany(mappedBy = "user",orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Learning> learnings;

    @OneToMany(mappedBy = "user",orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<UserGroupLearning> groupLearnings;

    @OneToMany(mappedBy = "user",orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Group> groups;

    @Lob
    @Basic(fetch=LAZY)
    private String tmp;

    @Column(columnDefinition = "int default 3")
    private int countSuccessful;

    @OneToMany(mappedBy = "user",orphanRemoval = true,
            cascade = CascadeType.REMOVE)
    private List<Feedback> feedbacks;

    @Column(columnDefinition = "bit(1) default 1")
    private boolean isViewAudio;

    @OneToMany(mappedBy = "user", orphanRemoval = true,
        cascade = CascadeType.ALL)
    private List<Session> sessions;

    public Session getActiveSession(){
        return sessions.stream().filter(Session::isState).findFirst().orElse(null);
    }
}
