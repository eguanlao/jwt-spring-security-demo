package org.zerhusen.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Component
class AuthenticationTokenFilterBean extends GenericFilterBean {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenParser tokenParser;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest httpRequest = (HttpServletRequest) request;
    final String authToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

    final Optional<String> subject = tokenParser.getSubject(authToken);

    if (subject.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
      final UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject.get());

      if (tokenParser.isTokenValid(authToken, userDetails)) {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    chain.doFilter(request, response);
  }

}
