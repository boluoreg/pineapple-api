package org.pineapple.pineappleapi.entity.vo;

import lombok.Data;

@Data
public class CaptchaVO {
    private String ticket;
    private String image;
}
