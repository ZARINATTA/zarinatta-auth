package oauth.auth;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import oauth.exception.ZarinattaException;
import oauth.exception.ZarinattaExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}") // application.properties 등에 보관한다.
    private String secretKey;

    private final static long ACCESS_TIME = 30 * 60 * 1000L; //30분

    private final static long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L; //7일

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰 생성
    public String createAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId); // JWT payload 에 저장되는 정보단위
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + ACCESS_TIME)) // 토큰 유효시각 설정 (30분)
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘과, secret 값
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String decodeAccessToken(String token) throws ZarinattaException {
        //TODO: validation 여기서 안할 때 문제 없을까 생각해봐야함
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject() != null ? claims.getSubject() :  null;
        } catch (ExpiredJwtException e) {
            throw new ZarinattaException(ZarinattaExceptionType.EXPIRED_TOKEN_ERROR);
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().build().parseClaimsJws(token);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("NOT_EXIST_REFRESH_TOKEN");
            return false;
        } catch (ExpiredJwtException e) {
            log.error("EXPIRED_TOKEN");
            return false;
        } catch (Exception e) {
            log.error("INTERNAL_SERVER_ERROR");
            return false;
        }
    }

}
