package br.com.axolot.animal.utils;

import br.com.axolot.animal.model.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static PasswordEncoder passwordEncoder;

    @Autowired
    public JwtUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static String generateToken(UserEntity userEntity) {
        String secretKey = "PfByAFLRYHCPJ9wdbo6JhAMjctna7ozBbP4Ywk9RASAM2mo9uw8shaxssoBQhs9T3iuIhfzB7nvwCStGOLcZLUQrifrTaueJSxn9HYPpvxzUlve8TAvD88DKyZBVQQ44xSFQzUhhzxbUyMDETetmayO9Z2MumVmS40y4cZHERyYJYGKJiaN6QVS4XXIobuyEoQKSo9BQliVc2Z3zU7zuVhTdwEu2k4T6KbmsbzvUd2pcy2EPolrUPWeapBIwNEqC";

        long expirationTime = 3600000;
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expirationTime);

        String token = Jwts.builder()
                .setSubject(userEntity.getUsername())
                .setId(userEntity.getId().toString())
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }

    public static Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static Boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
