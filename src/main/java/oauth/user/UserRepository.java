package oauth.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Transactional
    @Query(value = "UPDATE users SET " +
            "USER_DEVICE_TOKEN = CASE WHEN :token IS NOT NULL THEN :token ELSE token END, " +
            "USER_PHONE = CASE WHEN :phone IS NOT NULL THEN :phone ELSE email END " +
            "WHERE id = :id", nativeQuery = true)
    void update(@Param("id") String id, @Param("token") String token, @Param("phone") String phone);
}


