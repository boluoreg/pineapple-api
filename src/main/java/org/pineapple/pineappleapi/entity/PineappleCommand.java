package org.pineapple.pineappleapi.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PineappleCommand {
    private String cmd;
    private int count;
}
