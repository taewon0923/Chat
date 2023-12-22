package aa.chat1;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class TestClass {

    @Value("${jwt.token.secret}")
    private String key;
    private String secretKey;
    private Long expireTimeMs = 1000*60*60L;


    public String createToken(String userName, String key, Long expiredUntilMs) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredUntilMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String getUserName(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody()
                .get("userName", String.class);
    }

    @Test
    public void testTokenCreationAndExtraction() {
        String token = createToken("1111", secretKey, expireTimeMs);
        String user = getUserName(token, key);

        System.out.println("User from token: " + user);
    }
}
