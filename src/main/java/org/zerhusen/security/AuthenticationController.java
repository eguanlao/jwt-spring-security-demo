package org.zerhusen.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenParser tokenParser;

  @Autowired
  private UserDetailsService userDetailsService;

  @PostMapping("/auth")
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
      @RequestBody final AuthenticationRequest authenticationRequest)
      throws AuthenticationException {

    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authenticationRequest.getUsername(),
            authenticationRequest.getPassword()
        )
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    final String token = tokenParser.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(token));
  }

}
