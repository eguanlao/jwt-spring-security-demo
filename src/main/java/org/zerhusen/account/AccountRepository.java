package org.zerhusen.account;

import java.util.Optional;

public interface AccountRepository {

  Optional<Account> findByUsername(String username);

}
