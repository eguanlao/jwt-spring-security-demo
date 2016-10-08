package org.zerhusen.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

  @Autowired
  private TokenParser tokenParser;

  @Autowired
  private UserDetailsService userDetailsService;

  @GetMapping("/user")
  public AccountUserDetails getAuthenticatedUser(HttpServletRequest request) {
    final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    final Optional<String> subject = tokenParser.getSubject(token);
    if (subject.isPresent()) {
      return (AccountUserDetails) userDetailsService.loadUserByUsername(subject.get());
    }
    return null;
  }

}
