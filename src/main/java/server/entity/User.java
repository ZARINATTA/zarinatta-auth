package server.entity;

import jakarta.persistence.*;

import jakarta.annotation.Nullable;
import lombok.*;

@Entity(name="USERS")
@Table(name="USERS")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
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
}
