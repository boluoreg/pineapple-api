package org.pineapple.pineappleapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Pineapple;
import org.pineapple.pineappleapi.entity.dto.PineappleDTO;
import org.pineapple.pineappleapi.entity.vo.AnalysisVO;
import org.pineapple.pineappleapi.entity.vo.PineappleVO;
import org.pineapple.pineappleapi.repository.PineappleRepository;
import org.pineapple.pineappleapi.service.AnalysisMapper;
import org.pineapple.pineappleapi.service.PineappleMapper;
import org.pineapple.pineappleapi.service.PineappleService;
import org.springframework.kafka.annotation.KafkaListener;
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
}
