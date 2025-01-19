package org.pineapple.pineappleapi.service;

import org.pineapple.pineappleapi.entity.Account;
import org.pineapple.pineappleapi.entity.dto.RegisterDTO;
import org.pineapple.pineappleapi.entity.vo.AccountVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    Account findAccountByUsername(String username);

    AccountVO register(RegisterDTO dto);

    Account findAccount(UserDetails principal);

    AccountVO updatePassword(Account account, String newPassword);

    boolean assertPassword(Account account, String password);
}
