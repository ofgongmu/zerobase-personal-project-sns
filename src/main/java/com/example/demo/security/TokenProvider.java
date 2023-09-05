package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

  private final Key key;
  private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;

  public TokenProvider(@Value("${jwt.secret}") String secretKey) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public String createToken(String id, String role) {
    Claims claims = Jwts.claims().setSubject(id);
    claims.put("role", role);
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
        .signWith(this.key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    return new UsernamePasswordAuthenticationToken(getId(token), "", Stream.of("ROLE_USER").map(SimpleGrantedAuthority::new).toList());
  }

  public String getId(String token) {
    return Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody().getSubject();
  }

 public boolean checkIfValidToken(String token) {
    return parseClaims(token).getExpiration().after(new Date());
 }

  private Claims parseClaims(String token) {
    try {
      return (Claims) Jwts.parserBuilder().setSigningKey(this.key).build().parse(token).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
