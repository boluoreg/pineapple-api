package org.pineapple.pineappleapi.controller;

import lombok.RequiredArgsConstructor;
import org.pineapple.pineappleapi.entity.Point;
import org.pineapple.pineappleapi.entity.RestBean;
import org.pineapple.pineappleapi.entity.dto.CaptchaDTO;
import org.pineapple.pineappleapi.entity.vo.CaptchaVO;
import org.pineapple.pineappleapi.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;

    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;
    private static final int PINEAPPLE_WIDTH = 40;
    private static final int PINEAPPLE_HEIGHT = 40;

    @GetMapping
    public ResponseEntity<RestBean<CaptchaVO>> generateCaptcha() throws Exception {
        int randomizedBackground = new Random().nextInt(3);
        BufferedImage baseImage = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/background-" + randomizedBackground + ".jpg")));
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage pineapple = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/pineapple.png")));
        Graphics2D g = image.createGraphics();

        g.drawImage(baseImage, 0, 0, WIDTH, HEIGHT, null);

        g.setColor(Color.RED);

        List<Point> points = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            if (x + PINEAPPLE_WIDTH > WIDTH) {
                x = WIDTH - PINEAPPLE_WIDTH;
            }
            if (y + PINEAPPLE_HEIGHT > HEIGHT) {
                y = HEIGHT - PINEAPPLE_HEIGHT;
            }
            AffineTransform transform = new AffineTransform();
            transform.rotate(new Random().nextInt(90), x + (double) PINEAPPLE_WIDTH / 2, y + (double) PINEAPPLE_HEIGHT / 2);
            transform.translate(x, y);
            g.setTransform(transform);
            g.drawImage(pineapple, 0, 0, PINEAPPLE_WIDTH, PINEAPPLE_HEIGHT, null);
            g.setTransform(new AffineTransform());
            points.add(new Point(x, y));
        }

        for (int i = 0; i < 5; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            g.fillOval(x, y, 5, 5);
        }

        g.dispose();

        // save to redis
        return ResponseEntity.ok(RestBean.success(captchaService.add(points, image)));
    }

    @PostMapping
    public ResponseEntity<RestBean<?>> verify(@RequestBody CaptchaDTO dto) {
        boolean result;
        try {
            result = captchaService.verify(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.badRequest("Ticket expired"));
        }
        if (result) {
            return ResponseEntity.ok(RestBean.success());
        } else {
            return ResponseEntity.badRequest().body(RestBean.badRequest("Bad pineappleptcha"));
        }
    }
}
