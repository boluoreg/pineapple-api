package org.pineapple.pineappleapi.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Account;
import org.pineapple.pineappleapi.entity.dto.RegisterDTO;
import org.pineapple.pineappleapi.entity.vo.AccountVO;
import org.pineapple.pineappleapi.repository.AccountRepository;
import org.pineapple.pineappleapi.service.AccountMapper;
import org.pineapple.pineappleapi.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    @Value("${pineapple.account.username}")
    private String username;

    @Value("${pineapple.account.password}")
    private String password;

    @PostConstruct
    private void init() {
        if (accountRepository.count() == 0) {
            log.info("Registering the default admin account...");
            register(username, password, true);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return User.builder()
                .username(username)
                .password(account.getPassword())
                .roles(account.getRoles().toArray(String[]::new))
                .build();
    }

    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }

    @Override
    public AccountVO register(@NotNull RegisterDTO dto) {
        return this.register(dto.getUsername(), dto.getPassword(), false);
    }

    private @NotNull AccountVO register(String username, String password, boolean admin) {
        // find exists
        Optional<Account> existAccount = accountRepository.findByUsername(username);
        if (existAccount.isPresent()) {
            throw new IllegalArgumentException("Account already exists");
        }
        // check username
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));

        if (admin) {
            account.setRoles(List.of("USER", "ADMIN"));
        } else {
            account.setRoles(List.of("USER"));
        }
        log.info("Account {} was registered", account.getUsername());
        return accountMapper
                .toAccountVO(accountRepository.save(account));
    }

    @Override
    public Account findAccount(@NotNull UserDetails principal) {
        return accountRepository.findByUsername(principal.getUsername()).orElse(null);
    }

    @Override
    public AccountVO updatePassword(@NotNull Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        return accountMapper
                .toAccountVO(accountRepository.save(account));
    }

    @Override
    public boolean assertPassword(Account account, String password) {
        return passwordEncoder.matches(password, account.getPassword());
    }

    private boolean isValidUsername(String username) {
        if (username == null || username.length() < 5) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_-]+$");
    }
}
