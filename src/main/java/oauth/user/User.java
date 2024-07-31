package oauth.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name="USERS")
@Getter
@Builder
public class User {

    @Id
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
