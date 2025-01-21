package org.pineapple.pineappleapi.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@RedisHash(timeToLive = 3600)
public class Captcha {
    @Id
    private String id;

    private List<Point> solutions;
    private boolean solved;

    public boolean verify(@NotNull List<Point> clicks) {
        int threshold = 15;
        int correctClicks = 0;

        if (clicks.size() != solutions.size()) {
            return false;
        }

        for (Point userClick : clicks) {
            for (Point target : solutions) {
                if (Math.abs(userClick.getX() - target.getX()) <= threshold &&
                        Math.abs(userClick.getY() - target.getY()) <= threshold) {
                    correctClicks++;
                    break;
                }
            }
        }

        return correctClicks == solutions.size();
    }
}
