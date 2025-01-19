package org.pineapple.pineappleapi.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AccountVO {
    private String username;
    private List<String> roles;
}
