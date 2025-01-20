package org.pineapple.pineappleapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Pineapple;
import org.pineapple.pineappleapi.entity.PineappleCommand;
import org.pineapple.pineappleapi.entity.dto.HeartbeatDTO;
import org.pineapple.pineappleapi.entity.dto.PineappleDTO;
import org.pineapple.pineappleapi.entity.dto.PlantingPineappleDTO;
import org.pineapple.pineappleapi.entity.vo.AnalysisVO;
import org.pineapple.pineappleapi.entity.vo.PineappleVO;
import org.pineapple.pineappleapi.repository.PineappleRepository;
import org.pineapple.pineappleapi.service.AnalysisMapper;
import org.pineapple.pineappleapi.service.PineappleMapper;
import org.pineapple.pineappleapi.service.PineappleService;
import org.pineapple.pineappleapi.util.Const;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class PineappleServiceImpl implements PineappleService {
    private final PineappleRepository pineappleRepository;
    private final AnalysisMapper analysisMapper;
    private final PineappleMapper pineappleMapper;
    private final KafkaTemplate<String, PineappleCommand> kafkaTemplate;

    private final RedisTemplate<String, Boolean> booleanRedisTemplate;

    @KafkaListener(topics = "pineapple-reg")
    public void listen(@NotNull List<PineappleDTO> pineapples) {
        log.info("Received {} pineapples", pineapples.size());
        pineappleRepository.saveAll(
                pineapples.stream().map(pineapple -> {
                            if (!pineapple.isSuccess()) {
                                log.warn("A pineapple was broken {}", pineapple.getMsg());
                                return null;
                            }
                            Pineapple pa = new Pineapple();
                            pa.setUsername(pineapple.getUsername());
                            pa.setPassword(pineapple.getPassword());
                            log.info("Pineapple with username {} was received", pa.getUsername());
                            return pa;
                        })
                        .filter(Objects::nonNull)
                        .toList()
        );
        log.info("Pineapples received");
    }

    @KafkaListener(topics = "pineapple-heartbeat")
    public void handleHeartbeat(@NotNull HeartbeatDTO heartbeat) {
        log.info("Received heartbeat (working={})", heartbeat.isPlanting());
        booleanRedisTemplate.opsForValue().set(Const.IS_WORKING, heartbeat.isAlreadyWorking());
        booleanRedisTemplate.opsForValue().set(Const.IS_PLANTING, heartbeat.isPlanting());
    }

    @Override
    public AnalysisVO analysis() {
        return analysisMapper.toAnalysisVO();
    }

    @Override
    public PineappleVO fetch() {
        Pineapple pineapple = pineappleRepository.findFirstByOrderByIdAsc()
                .orElse(null);
        if (pineapple == null) {
            return null;
        }
        log.info("Pineapple with username {} was ate", pineapple.getUsername());
        pineappleRepository.delete(pineapple);
        return pineappleMapper.toPineappleVO(pineapple);
    }

    @Override
    public void plant(PlantingPineappleDTO dto) {
        if (analysisMapper.toAnalysisVO().isPlanting()) {
            throw new IllegalArgumentException("菠萝机无响应或工作未完成,主播再等等");
        }
        kafkaTemplate.send("pineapple-commands", "pineapple-commands", PineappleCommand.builder()
                .cmd("start")
                .count(dto.getCount())
                .build());
    }
}
