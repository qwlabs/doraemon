package com.qwlabs.excel.read;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReadResult<T> {
    private final List<T> data;
}
