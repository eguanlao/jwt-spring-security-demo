package org.zerhusen.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerhusen.account.Account;
import org.zerhusen.account.AccountRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountUserDetailsService implements UserDetailsService {

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final Optional<Account> account = accountRepository.findByUsername(username);

    if (account.isPresent()) {
      return AccountUserDetails.builder()
          .authorities(account.get().getAuthorities().stream()
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList()))
          .password(account.get().getPassword())
          .username(account.get().getUsername())
          .accountNonExpired(true)
          .accountNonLocked(true)
          .credentialsNonExpired(true)
          .enabled(account.get().isEnabled())
          .build();
    }

    throw new UsernameNotFoundException(String.format("No account found with username '%s'.", username));
  }

}
