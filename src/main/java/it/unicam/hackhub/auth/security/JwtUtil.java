package it.unicam.hackhub.auth.security;

import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Date;

@Component 
public class JwtUtil {

    private static final String SECRET = "mySecretKey123";

    public String generateToken(String userId, String role) {
        String tokenData = userId + ":" + role + ":" + new Date().getTime();
        return Base64.getEncoder().encodeToString((tokenData + SECRET).getBytes());
    }

    public String validateAndGetUserId(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            if (!decoded.endsWith(SECRET)) {
                throw new IllegalStateException("Token non valido");
            }

            String data = decoded.replace(SECRET, "");
            return data.split(":")[0];

        } catch (Exception e) {
            throw new IllegalStateException("Token non valido");
        }
    }
}