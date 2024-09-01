package server.entity;

import jakarta.persistence.*;

import jakarta.annotation.Nullable;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name="USERS")
@Table(name="USERS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @Column(name="USER_ID", unique = true)
    private String id;

    @Column(name="USER_EMAIL")
    private String userEmail;

    @Column(name="USER_NICK")
    private String userNick;

    @Column(name="USER_PHONE")
    @Nullable
    private String userPhoneNumber;

    @Column(name="USER_DEVICE_TOKEN")
    @Nullable
    private String userDeviceToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BookMark> bookmarks = new ArrayList<>();
}
