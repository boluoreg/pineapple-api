package org.pineapple.pineappleapi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PineappleDTO {
    private boolean success;
    private String msg;
    private String username;
    private String password;
}
