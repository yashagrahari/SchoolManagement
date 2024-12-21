package com.school.school_management_system.security.jwt;

import com.school.school_management_system.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

//    @Value("${jwt.secret}")
    private String jwtSecret = "0eb392af14f895841f021531a773be8cf7abafb35edf4a69a5becc1991862c63eb191e4dcbf5fbdd9485724317fd28fcda3e543b7288e3dc49e6cf0ad8304c06088cd59b4d75aeb01f889dcc3635838d4e8ec5bb07a0d62d0d0314ff1787ccad445d43cf9418adb17d8a6e49f627aad22c0829a675a06f0d801cdb46ce99e21a55b23ddddd5570bd19d483e971ba2f674a26701d075db94c18013d1aa52324b916a957bf756829a461af18c98a39f95fcbebc40e6cdc6625689291edcc9306a2e20a4939e640d76122a7528e8fc9d03e4e8d99763d5a0d60af335aae764e66dd67c6d9ba7fc124151be51d335795eec5a43068a4a246c0cc9c7e44eea47f39bd5f834399254463091a139106b21c4c056784663b19600e0110674bf2aed21cfe92d4b358b5ae66571a9e5c83105e6b232a5447ad9392c7f7c27b4930236d3ba69ed7150cbb742b3c630bce7bd3f61ca931b1cb132c0ff99ed89d62ee1aa75c0e6af0a8d549fb7ad2f256a0a5b2ef68c539b022f9d9bc543d0d17abe43f43e62d41f0cdb4e1a69acaac5306a7882254f1c4746aad68bcb3d8d6c154f65b920562c03a84d3d640b3558c17fa10c7e7d05075a0367c30b37aeff02a84633d804b0fe17ce8fa15bfc99affb28c64916ab89d1fcde2c7ebdecd8f8e6d94ed6e806912c068ff92ac1f53d9ca94b2d93103f9efd72d7675458564823e9027c852120917";

//    @Value("${jwt.expiration}")
    private int jwtExpirationMs = 3600000;

    /**
     * Generates JWT token for authenticated user
     * @param authentication Authentication object containing user details
     * @return JWT token string
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userPrincipal.getId());
        claims.put("roles", userPrincipal.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Generates token for refreshing user session
     * @param username User's username
     * @return Refresh token string
     */
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (jwtExpirationMs * 2))) // Longer expiration for refresh token
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Extracts username from JWT token
     * @param token JWT token
     * @return Username string
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Gets specific claim from JWT token
     * @param token JWT token
     * @param claimName Name of the claim to retrieve
     * @return Claim value
     */
    public Object getClaimFromJwtToken(String token, String claimName) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .get(claimName);
    }

    /**
     * Validates JWT token
     * @param authToken JWT token to validate
     * @return boolean indicating if token is valid
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Checks if token is expired
     * @param token JWT token
     * @return boolean indicating if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Gets remaining validity time of token in milliseconds
     * @param token JWT token
     * @return remaining time in milliseconds
     */
    public long getTokenRemainingValidityTime(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            return expiration.getTime() - System.currentTimeMillis();
        } catch (ExpiredJwtException e) {
            return -1;
        }
    }

    /**
     * Extracts all claims from token
     * @param token JWT token
     * @return Claims object
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
