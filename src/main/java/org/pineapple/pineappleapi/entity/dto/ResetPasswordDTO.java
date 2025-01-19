package org.pineapple.pineappleapi.entity.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String oldPassword;
    private String password;
}
