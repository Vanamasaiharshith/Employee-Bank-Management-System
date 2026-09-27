package com.example.employeebank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JwtService - Handles everything related to JWT tokens.
 *
 * JWT = JSON Web Token
 * A JWT has 3 parts separated by dots: Header.Payload.Signature
 *
 * Example token:
 *   eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aWpheSIsImlhdCI6MTYwMH0.abc123
 *
 * - Header: algorithm used (HS256)
 * - Payload: contains the username (called 'subject') and expiry time
 * - Signature: ensures the token hasn't been tampered with
 *
 * @Service tells Spring to create one instance of this class
 *          and make it available for injection with @Autowired or constructor injection.
 */
@Service
public class JwtService {

    /**
     * The secret key is read from application.properties (jwt.secret).
     * It is used to SIGN the token when creating it,
     * and to VERIFY the token when validating it.
     *
     * @Value("${jwt.secret}") injects the value from application.properties.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Token expiration time in milliseconds, read from application.properties.
     * Default: 86400000 ms = 24 hours
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Converts the secret string into a cryptographic key object
     * that the JJWT library uses to sign and verify tokens.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * GENERATE a JWT token for a given username.
     *
     * Steps:
     *  1. Set the subject (username)
     *  2. Set issued-at time (now)
     *  3. Set expiry time (now + 24 hours)
     *  4. Sign with our secret key
     *  5. Build and return the token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)                                      // who this token belongs to
                .issuedAt(new Date(System.currentTimeMillis()))         // when it was created
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // when it expires
                .signWith(getSigningKey())                              // sign with secret key
                .compact();                                             // build the token string
    }

    /**
     * EXTRACT the username (subject) from a JWT token.
     *
     * The token is parsed and verified using our secret key.
     * If the token is invalid or tampered, JJWT throws an exception.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * CHECK if a token is valid.
     *
     * A token is valid if:
     *  1. The username in the token matches the given username
     *  2. The token is not expired
     */
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Check if the token has expired by comparing expiry date with current time.
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Parse the JWT and return all claims (the payload section of the token).
     *
     * Claims contain: subject (username), issued-at, expiry, and any custom data.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // use our secret key to verify the signature
                .build()
                .parseSignedClaims(token)    // parse the token
                .getPayload();               // get the payload (claims)
    }
}
