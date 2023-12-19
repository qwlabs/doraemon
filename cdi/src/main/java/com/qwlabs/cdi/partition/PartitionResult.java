package com.qwlabs.cdi.partition;

import com.qwlabs.cdi.CDIMessages;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartitionResult {

    public static <R> R get(Stream<R> data, Class<R> returnType) {
        if (Stream.class.isAssignableFrom(returnType)) {
            return (R) getStreamData(data);
        }
        if (Set.class.isAssignableFrom(returnType)) {
            return (R) getSetData(data);
        }
        if (List.class.isAssignableFrom(returnType)) {
            return (R) getListData(data);
        }
        if (Integer.TYPE.isAssignableFrom(returnType) || Integer.class.isAssignableFrom(returnType)) {
            return (R) getIntegerData(data);
        }
        if (Long.TYPE.isAssignableFrom(returnType) || Long.class.isAssignableFrom(returnType)) {
            return (R) getLongData(data);
        }
        throw CDIMessages.INSTANCE.codeError("PartitionResult can not support type " + returnType.getName());
    }

    private static <R> Long getLongData(Stream<R> data) {
        return data.mapToLong(item -> (Long) item).sum();
    }

    private static <R> Integer getIntegerData(Stream<R> data) {
        return data.mapToInt(item -> (Integer) item).sum();
    }

    private static <R> Set<?> getSetData(Stream<R> data) {
        return data.map(item -> (Set<?>) item)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    private static <R> List<?> getListData(Stream<R> data) {
        return data.map(item -> (List<?>) item)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    private static <R> Stream<?> getStreamData(Stream<R> data) {
        return data.flatMap(item -> (Stream<?>) item);
    }
}
