package com.seokju.study.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Test
  public void findByUsername() {
    // Given
    String password = "seokju";
    String username = "seokju@test.com";

    Account account = Account.builder()
        .email(username)
        .password(password)
        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
        .build();

    accountRepository.save(account);

    // When
    UserDetailsService userDetailsService = (UserDetailsService) accountService;
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // Then
    then(userDetails.getPassword()).isEqualTo(password);
  }

  @Test()
  public void findByUsernameFail() {
    String username = "random@test.com";

    try {
      accountService.loadUserByUsername(username);
      fail("supposed to be failed");

    } catch (UsernameNotFoundException e) {
      then(e.getMessage()).contains(username);
    }
  }
}