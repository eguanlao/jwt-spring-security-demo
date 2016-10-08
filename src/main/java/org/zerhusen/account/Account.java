package org.zerhusen.account;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {

  private String username;
  private String password;
  private List<String> authorities;
  private boolean enabled;

}
