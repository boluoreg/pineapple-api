package org.pineapple.pineappleapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Captcha;
import org.pineapple.pineappleapi.entity.Point;
import org.pineapple.pineappleapi.entity.dto.CaptchaDTO;
import org.pineapple.pineappleapi.entity.vo.CaptchaVO;
import org.pineapple.pineappleapi.repository.CaptchaRepository;
import org.pineapple.pineappleapi.service.CaptchaMapper;
import org.pineapple.pineappleapi.service.CaptchaService;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final CaptchaRepository captchaRepository;
    private final CaptchaMapper captchaMapper;

    @Override
    public CaptchaVO add(List<Point> points, BufferedImage image) throws IOException {
        Captcha captcha = new Captcha();
        captcha.setSolutions(points);
        Captcha saved = captchaRepository.save(captcha);
        return captchaMapper.toCaptchaVO(saved, image);
    }

    @Override
    public boolean verify(@NotNull CaptchaDTO dto) {
        Captcha captcha = captchaRepository.findById(dto.getTicket()).orElseThrow(
                () -> new IllegalArgumentException("Invalid ticket")
        );
        if (captcha.isSolved()) return true;
        boolean result = captcha.verify(dto.getPoints());
        if (result) {
            // mark the captcha as verified
            captcha.setSolved(true);
            captchaRepository.save(captcha);
        }
        return result;
    }

    @Override
    public boolean isSolved(String ticket) {
        if (ticket == null) return false;
        Captcha captcha = captchaRepository.findById(ticket).orElse(null);
        if (captcha == null) return false;
        return captcha.isSolved();
    }
}
