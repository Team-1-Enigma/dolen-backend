package com.enigma.dolen.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.dolen.model.entity.UserCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${app.dolen.jwt.jwt-secret}")
    private String jwtSecret;
    @Value("${app.dolen.jwt.app-name}")
    private String appName;
    @Value("${app.dolen.jwt.jwt-expiration}")
    private Long tokenExpiration;

    public String generateToken(UserCredential userCredential) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(userCredential.getId())
                    .withExpiresAt(Instant.now().plusSeconds(tokenExpiration))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", userCredential.getRole().getName().name())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, String> getUserInfoByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            return Map.of(
                    "credentialId", decodedJWT.getSubject(),
                    "role", decodedJWT.getClaim("role").asString()
            );
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
