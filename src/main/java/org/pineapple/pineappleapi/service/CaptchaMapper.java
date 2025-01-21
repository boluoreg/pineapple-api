package org.pineapple.pineappleapi.service;

import org.pineapple.pineappleapi.entity.Captcha;
import org.pineapple.pineappleapi.entity.vo.CaptchaVO;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface CaptchaMapper {
    CaptchaVO toCaptchaVO(Captcha captcha, BufferedImage image) throws IOException;
}
