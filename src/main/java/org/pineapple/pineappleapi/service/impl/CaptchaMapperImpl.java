package org.pineapple.pineappleapi.service.impl;

import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Captcha;
import org.pineapple.pineappleapi.entity.vo.CaptchaVO;
import org.pineapple.pineappleapi.service.CaptchaMapper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class CaptchaMapperImpl implements CaptchaMapper {
    @Override
    public CaptchaVO toCaptchaVO(@NotNull Captcha captcha, BufferedImage image) throws IOException {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setTicket(captcha.getId());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        captchaVO.setImage(Base64.getEncoder().encodeToString(out.toByteArray()));
        return captchaVO;
    }
}
