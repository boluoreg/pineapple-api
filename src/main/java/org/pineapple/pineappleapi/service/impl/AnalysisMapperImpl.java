package org.pineapple.pineappleapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.pineapple.pineappleapi.entity.vo.AnalysisVO;
import org.pineapple.pineappleapi.repository.PineappleRepository;
import org.pineapple.pineappleapi.service.AnalysisMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisMapperImpl implements AnalysisMapper {
    private final PineappleRepository pineappleRepository;

    @Override
    public AnalysisVO toAnalysisVO() {
        AnalysisVO analysisVO = new AnalysisVO();
        analysisVO.setTotalPineapples(pineappleRepository.count());
        return analysisVO;
    }
}
