package org.pineapple.pineappleapi.service;

import org.pineapple.pineappleapi.entity.Point;
import org.pineapple.pineappleapi.entity.dto.CaptchaDTO;
import org.pineapple.pineappleapi.entity.vo.CaptchaVO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface CaptchaService {
    CaptchaVO add(List<Point> points, BufferedImage image) throws IOException;

    boolean verify(CaptchaDTO dto);

    boolean isSolved(String ticket);
}
