package com.example.demo.security;

import com.example.demo.entity.Account;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class AccountUserDetails implements UserDetails {

  private Account account;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Stream.of(account.getRole()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return account.getPassword();
  }

  @Override
  public String getUsername() {
    return account.getId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return account.getIsActivated();
  }

  @Override
  public boolean isAccountNonLocked() {
    return account.getIsActivated();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return account.getIsActivated();
  }
}
