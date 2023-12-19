package com.qwlabs.cdi.partition;

import com.qwlabs.cdi.CDIMessages;
import com.qwlabs.lang.BufferedCall;
import com.qwlabs.lang.C2;
import com.qwlabs.lang.F2;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Partition()
@Priority(1)
@Interceptor
@Slf4j
public class PartitionInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        var partition = context.getMethod().getAnnotation(Partition.class);
        PartitionByParameter partitionByParameter = lookupPartitionBy(context);
        if (!partitionByParameter.needCall(partition.size())) {
            return context.proceed();
        }
        LOGGER.info("Start Partition on {}.{} method", context.getTarget().getClass().getName(), context.getMethod().getName());
        var bufferedCall = BufferedCall.of(partition.size());
        partitionByParameter.add(bufferedCall);
        return process(bufferedCall, partitionByParameter, context);
    }

    private <E, R> R process(BufferedCall<E, R> bufferedCall, PartitionByParameter partitionByParameter, InvocationContext context) {
        var finalData = bufferedCall.call(data -> {
            try {
                return partitionByParameter.call(data, context);
            } catch (Exception e) {
                LOGGER.error("Can not invoke method {}", context.getMethod().getName());
                throw new RuntimeException(e);
            }
        });
        return PartitionResult.get(finalData, (Class<R>) context.getMethod().getReturnType());
    }

    private PartitionByParameter lookupPartitionBy(InvocationContext context) {
        var max = context.getParameters().length;
        var allParameterAnnotations = context.getMethod().getParameterAnnotations();
        for (int index = 0; index < max; index++) {
            var parameterAnnotations = allParameterAnnotations[index];
            if (hasPartitionBy(parameterAnnotations)) {
                return buildPartitionByParameter(context, index);
            }
        }
        throw CDIMessages.INSTANCE.codeError("Can not found @PartitionBy in method:" + context.getMethod().getName());
    }

    private boolean hasPartitionBy(Annotation[] parameterAnnotations) {
        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof PartitionBy) {
                return true;
            }
        }
        return false;
    }

    private PartitionByParameter buildPartitionByParameter(InvocationContext context, int index) {
        return PartitionByParameter.builder()
            .index(index)
            .data(context.getParameters()[index])
            .type(context.getMethod().getParameterTypes()[index])
            .build();
    }

    @Builder
    @AllArgsConstructor
    private static class PartitionByParameter {
        private final int index;
        private final Object data;
        private final Class<?> type;

        public Class<?> dataType() {
            return F2.ifPresent(data, Object::getClass, this.type);
        }

        public <E, R> void add(BufferedCall<E, R> bufferedCall) {
            Class<?> type = dataType();
            if (Stream.class.isAssignableFrom(type)) {
                bufferedCall.add((Stream<E>) data);
                return;
            }
            if (Set.class.isAssignableFrom(type)) {
                bufferedCall.add((Set<E>) data);
                return;
            }
            if (List.class.isAssignableFrom(type)) {
                bufferedCall.add((List<E>) data);
                return;
            }
            if (Collection.class.isAssignableFrom(type)) {
                bufferedCall.add((Collection<E>) data);
                return;
            }
            throw CDIMessages.INSTANCE.codeError("PartitionBy can not support type " + type);
        }

        public <R, E> R call(List<E> data, InvocationContext context) throws Exception {
            var parameters = context.getParameters();
            var newParameters = Arrays.copyOf(parameters, parameters.length);
            newParameters[index] = transformData(data);
            context.setParameters(newParameters);
            return (R) context.proceed();
        }

        private <E> Object transformData(List<E> data) {
            var type = dataType();
            if (Stream.class.isAssignableFrom(type)) {
                return data.stream();
            }
            if (Set.class.isAssignableFrom(type)) {
                return C2.set(data);
            }
            if (List.class.isAssignableFrom(type)) {
                return data;
            }
            if (Collection.class.isAssignableFrom(type)) {
                return data;
            }
            throw CDIMessages.INSTANCE.codeError("PartitionBy can not support type " + type.getName());
        }

        public boolean needCall(int size) {
            if (Collection.class.isAssignableFrom(type)) {
                return ((Collection) data).size() > size;
            }
            return true;
        }
    }


}
