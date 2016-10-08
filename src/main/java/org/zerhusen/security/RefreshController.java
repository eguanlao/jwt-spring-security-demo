package org.zerhusen.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RefreshController {

  @Autowired
  private TokenParser tokenParser;

  @Autowired
  private UserDetailsService userDetailsService;

  @GetMapping("/refresh")
  public ResponseEntity<String> refreshAndGetAuthenticationToken(HttpServletRequest request) {
    final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    final Optional<String> username = tokenParser.getSubject(token);

    if (username.isPresent()) {
      final User user = (User) userDetailsService.loadUserByUsername(username.get());
      if (tokenParser.canTokenBeRefreshed(token, user.isCredentialsNonExpired())) {
        return ResponseEntity.ok(tokenParser.refreshToken(token));
      }
    }

    return ResponseEntity.badRequest().body(null);
  }

}
