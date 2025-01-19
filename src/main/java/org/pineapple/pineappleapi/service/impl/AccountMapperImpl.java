package org.pineapple.pineappleapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Account;
import org.pineapple.pineappleapi.entity.vo.AccountVO;
import org.pineapple.pineappleapi.service.AccountMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountMapperImpl implements AccountMapper {
    @Override
    public @NotNull AccountVO toAccountVO(@NotNull Account source) {
        AccountVO vo = new AccountVO();
        vo.setUsername(source.getUsername());
        vo.setRoles(source.getRoles());
        return vo;
    }
}
