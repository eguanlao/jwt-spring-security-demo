package org.zerhusen.account;

import com.google.common.collect.Lists;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAccountRepository implements AccountRepository {

  private final Map<String, Account> accountMap;

  public InMemoryAccountRepository() {
    accountMap = new ConcurrentHashMap<>();

    accountMap.put("admin", Account.builder()
        .username("admin")
        .password("$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC")
        .authorities(Lists.newArrayList("ROLE_USER", "ROLE_ADMIN"))
        .enabled(true)
        .build());

    accountMap.put("user", Account.builder()
        .username("user")
        .password("$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC")
        .authorities(Lists.newArrayList("ROLE_USER"))
        .enabled(true)
        .build());

    accountMap.put("disabled", Account.builder()
        .username("disabled")
        .password("$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC")
        .authorities(Lists.newArrayList("ROLE_USER"))
        .build());
  }

  @Override
  public Optional<Account> findByUsername(String username) {
    return Optional.ofNullable(accountMap.get(username));
  }

}
