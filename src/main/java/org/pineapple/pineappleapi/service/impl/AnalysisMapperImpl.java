package org.pineapple.pineappleapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.pineapple.pineappleapi.entity.vo.AnalysisVO;
import org.pineapple.pineappleapi.repository.PineappleRepository;
import org.pineapple.pineappleapi.service.AnalysisMapper;
import org.pineapple.pineappleapi.util.Const;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisMapperImpl implements AnalysisMapper {
    private final PineappleRepository pineappleRepository;
    private final RedisTemplate<String, Boolean> booleanRedisTemplate;

    @Override
    public AnalysisVO toAnalysisVO() {
        AnalysisVO analysisVO = new AnalysisVO();
        analysisVO.setTotalPineapples(pineappleRepository.count());
        analysisVO.setAlreadyWorking(booleanRedisTemplate.opsForValue().get(Const.IS_WORKING) == Boolean.TRUE);
        analysisVO.setPlanting(booleanRedisTemplate.opsForValue().get(Const.IS_PLANTING) == Boolean.TRUE);
        return analysisVO;
    }
}
