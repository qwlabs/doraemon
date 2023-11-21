package com.qwlabs.excel.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReadOptions<T> {
    private final Class<T> type;
    @Builder.Default
    private final ReadMode readMode = ReadMode.ROW;
    @Builder.Default
    private final int headIndex = 0;
    @Builder.Default
    private final int dataStartIndex = 1;
    @Builder.Default
    private final int dataLimit = Integer.MAX_VALUE;
}
