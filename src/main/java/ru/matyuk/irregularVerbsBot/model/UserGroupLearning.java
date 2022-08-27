package ru.matyuk.irregularVerbsBot.model;

import lombok.Data;
import ru.matyuk.irregularVerbsBot.model.id.UserGroupId;

import javax.persistence.*;

@Entity(name = "tg_users_group_learning")
@Data
public class UserGroupLearning {

    @EmbeddedId
    private UserGroupId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

}
