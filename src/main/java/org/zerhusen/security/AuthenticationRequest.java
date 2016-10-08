package org.zerhusen.security;

import java.io.Serializable;

import lombok.Data;

@Data
class AuthenticationRequest implements Serializable {

  private static final long serialVersionUID = 2L;

  private String username;
  private String password;

}
