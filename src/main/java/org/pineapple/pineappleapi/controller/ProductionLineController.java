package org.pineapple.pineappleapi.controller;

import lombok.RequiredArgsConstructor;
import org.pineapple.pineappleapi.entity.RestBean;
import org.pineapple.pineappleapi.entity.dto.PlantingPineappleDTO;
import org.pineapple.pineappleapi.entity.vo.PlantingPineappleVO;
import org.pineapple.pineappleapi.service.PineappleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/production-line")
public class ProductionLineController {
    private final PineappleService pineappleService;

    @PostMapping("planting")
    public ResponseEntity<RestBean<PlantingPineappleVO>> planting(@RequestBody PlantingPineappleDTO dto) {
        try {
            pineappleService.plant(dto);
            return ResponseEntity.ok(RestBean.success(new PlantingPineappleVO(dto.getCount())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RestBean.failure(503, e.getMessage()));
        }
    }
}
