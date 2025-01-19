package org.pineapple.pineappleapi.service;

import org.pineapple.pineappleapi.entity.Account;
import org.pineapple.pineappleapi.entity.vo.AuthorizeVO;

public interface AuthorizeMapper {
    AuthorizeVO toAuthorizeVO(Account account, String token);
}
