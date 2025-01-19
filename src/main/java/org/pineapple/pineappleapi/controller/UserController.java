package org.pineapple.pineappleapi.controller;

import lombok.RequiredArgsConstructor;
import org.pineapple.pineappleapi.entity.Account;
import org.pineapple.pineappleapi.entity.RestBean;
import org.pineapple.pineappleapi.entity.dto.RegisterDTO;
import org.pineapple.pineappleapi.entity.dto.ResetPasswordDTO;
import org.pineapple.pineappleapi.entity.vo.AccountVO;
import org.pineapple.pineappleapi.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final AccountService accountService;

    @PostMapping("register")
    public ResponseEntity<RestBean<AccountVO>> register(@RequestBody RegisterDTO dto) {
        try {
            AccountVO account = accountService.register(dto);
            return ResponseEntity.ok(RestBean.success(account));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestBean.badRequest(e.getMessage()));
        }
    }

    @PostMapping("resetPassword")
    public ResponseEntity<RestBean<AccountVO>> resetPassword(@AuthenticationPrincipal User user, @RequestBody ResetPasswordDTO dto) {
        if (dto.getOldPassword().equals(dto.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestBean.badRequest("You cannot set the same password"));
        }
        Account account = accountService.findAccount(user);
        if (!accountService.assertPassword(account, dto.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestBean.badRequest("Invalid old password"));
        }
        return ResponseEntity.ok(RestBean.success(accountService.updatePassword(account, dto.getPassword())));
    }
}
