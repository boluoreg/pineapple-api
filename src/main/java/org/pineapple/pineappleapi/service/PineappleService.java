package org.pineapple.pineappleapi.service;

import org.pineapple.pineappleapi.entity.vo.AnalysisVO;
import org.pineapple.pineappleapi.entity.vo.PineappleVO;

public interface PineappleService {
    AnalysisVO analysis();

    PineappleVO fetch();
}
