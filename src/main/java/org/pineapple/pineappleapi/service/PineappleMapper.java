package org.pineapple.pineappleapi.service;

import org.pineapple.pineappleapi.entity.Pineapple;
import org.pineapple.pineappleapi.entity.vo.PineappleVO;

public interface PineappleMapper {
    PineappleVO toPineappleVO(Pineapple pineapple);
}
