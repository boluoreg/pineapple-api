package org.pineapple.pineappleapi.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Slf4j
@RedisHash(timeToLive = 3600)
public class Captcha {
    @Id
    private String id;

    private List<Point> solutions;
    private boolean solved;

    public boolean verify(@NotNull List<Point> clicks) {
        int threshold = 40;
        int correctClicks = 0;

        if (clicks.size() != solutions.size()) {
            return false;
        }

        Set<Point> matchedTargets = new HashSet<>();

        for (Point userClick : clicks) {
            for (Point target : solutions) {
                if (!matchedTargets.contains(target) &&
                        Math.abs(userClick.getX() - target.getX()) <= threshold &&
                        Math.abs(userClick.getY() - target.getY()) <= threshold) {

                    correctClicks++;
                    matchedTargets.add(target); // mark matched
                    break;
                }
            }
        }


        return correctClicks == solutions.size();
    }
}
