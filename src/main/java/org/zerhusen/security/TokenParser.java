package org.zerhusen.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
class TokenParser implements Serializable {

  private static final long serialVersionUID = -2L;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  String generateToken(final UserDetails userDetails) {
    final Map<String, Object> claims = new HashMap<>();
    claims.put(Claims.SUBJECT, userDetails.getUsername());
    claims.put(Claims.ISSUED_AT, new Date());
    return generateToken(claims);
  }

  String generateToken(final Map<String, Object> claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(generateExpirationDate())
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  Optional<String> getSubject(String token) {
    final Claims claims = parse(token);
    return Optional.ofNullable(claims.getSubject());
  }

  private Optional<Date> getIssuedAt(String token) {
    final Claims claims = parse(token);
    return Optional.ofNullable(claims.getIssuedAt());
  }

  private Optional<Date> getExpiration(String token) {
    final Claims claims = parse(token);
    return Optional.ofNullable(claims.getExpiration());
  }

  private Claims parse(String token) {
    try {
      return Jwts.parser()
          .setSigningKey(secret)
          .parseClaimsJws(token)
          .getBody();
    } catch (final Exception e) {
      return Jwts.claims();
    }
  }

  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + expiration * 1000);
  }

  private boolean isTokenExpired(String token) {
    final Optional<Date> expiration = getExpiration(token);
    return !expiration.isPresent() || expiration.get().before(new Date());
  }

  boolean canTokenBeRefreshed(final String token, final boolean credentialsNonExpired) {
    final Optional<Date> createdDate = getIssuedAt(token);
    return createdDate.isPresent()
        && credentialsNonExpired
        && !isTokenExpired(token);
  }

  String refreshToken(final String token) {
    final Claims claims = parse(token);
    claims.put(Claims.ISSUED_AT, new Date());
    return generateToken(claims);
  }

  boolean isTokenValid(final String token, final UserDetails userDetails) {
    final Optional<String> username = getSubject(token);
    final Optional<Date> createdDate = getIssuedAt(token);

    return username.isPresent()
        && createdDate.isPresent()
        && username.get().equals(userDetails.getUsername())
        && !isTokenExpired(token)
        && userDetails.isCredentialsNonExpired();
  }

}