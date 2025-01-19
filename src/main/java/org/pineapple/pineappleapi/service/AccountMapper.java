package org.pineapple.pineappleapi.service;

import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Account;
import org.pineapple.pineappleapi.entity.vo.AccountVO;

public interface AccountMapper {
    @NotNull AccountVO toAccountVO(@NotNull Account source);
}
