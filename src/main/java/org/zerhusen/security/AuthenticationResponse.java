package org.zerhusen.security;

import java.io.Serializable;

import lombok.Data;

@Data
class AuthenticationResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String token;

  AuthenticationResponse(final String token) {
    this.token = token;
  }

}
