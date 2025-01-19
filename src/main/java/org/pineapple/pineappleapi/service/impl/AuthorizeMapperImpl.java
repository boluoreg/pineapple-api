package org.pineapple.pineappleapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Account;
import org.pineapple.pineappleapi.entity.vo.AuthorizeVO;
import org.pineapple.pineappleapi.service.AuthorizeMapper;
import org.pineapple.pineappleapi.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizeMapperImpl implements AuthorizeMapper {
    private final JwtUtil jwtUtil;

    @Override
    public AuthorizeVO toAuthorizeVO(@NotNull Account account, String token) {
        AuthorizeVO vo = new AuthorizeVO();
        vo.setUsername(account.getUsername());
        vo.setRoles(account.getRoles());
        vo.setExpire(jwtUtil.getExpireDate().getTime());
        vo.setToken(token);
        return vo;
    }
}
