package org.pineapple.pineappleapi.service.impl;

import org.jetbrains.annotations.NotNull;
import org.pineapple.pineappleapi.entity.Pineapple;
import org.pineapple.pineappleapi.entity.vo.PineappleVO;
import org.pineapple.pineappleapi.service.PineappleMapper;
import org.springframework.stereotype.Service;

@Service
public class PineappleMapperImpl implements PineappleMapper {
    @Override
    public PineappleVO toPineappleVO(@NotNull Pineapple pineapple) {
        PineappleVO pineappleVO = new PineappleVO();
        pineappleVO.setUsername(pineapple.getUsername());
        pineappleVO.setPassword(pineapple.getPassword());
        return pineappleVO;
    }
}
