package com.app.final_project.security.jwt;

import com.app.final_project.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration-day}")
    private long refreshExpirationDay;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Map<String, Object> extractInfoToken(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> map = new HashMap<>();
        map.put("id", claims.get("id"));
        map.put("username", claims.getSubject());
        map.put("authorities", claims.get("authorities"));
        return map;
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            User userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }


    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        //put id,username,authorities
        extraClaims.put("id", user.getUser_id());
        List<String> authorityStrings = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        extraClaims.put("authorities", authorityStrings);

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private String buildRefreshToken(User user, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUser_id());
        claims.put("type", "refresh"); // 👈 Phân biệt rõ là refresh token

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(User userDetails) {
        return buildRefreshToken(userDetails, refreshExpirationDay * 24 * 60 * 60 * 1000); // Convert days to milliseconds
    }
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwtToken);
            return true; // Token is valid
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
            // Token is invalid
        }
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
