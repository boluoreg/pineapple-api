package org.pineapple.pineappleapi.entity.dto;

import lombok.Data;
import org.pineapple.pineappleapi.entity.Point;

import java.util.List;

@Data
public class CaptchaDTO {
    private String ticket;
    private List<Point> points;
}
