package org.pineapple.pineappleapi.controller;

import lombok.RequiredArgsConstructor;
import org.pineapple.pineappleapi.entity.RestBean;
import org.pineapple.pineappleapi.entity.vo.AnalysisVO;
import org.pineapple.pineappleapi.entity.vo.PineappleVO;
import org.pineapple.pineappleapi.service.CaptchaService;
import org.pineapple.pineappleapi.service.PineappleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pineapple")
public class PineappleController {
    private final PineappleService pineappleService;
    private final CaptchaService captchaService;

    @GetMapping
    public ResponseEntity<RestBean<AnalysisVO>> analysis() {
        return ResponseEntity.ok(RestBean.success(pineappleService.analysis()));
    }

    @GetMapping("get")
    public ResponseEntity<RestBean<PineappleVO>> getPineapple(@RequestParam(required = false) String ticket) {
        if (!captchaService.isSolved(ticket)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RestBean.failure(403, "请完成菠萝🍍人验证,来防止赛博菠萝人"));
        }
        PineappleVO data = pineappleService.fetch();
        if (data == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RestBean.failure(503, "所有的🍍菠萝都被吃掉了!"));
        }
        return ResponseEntity.ok(RestBean.success(data));
    }
}
