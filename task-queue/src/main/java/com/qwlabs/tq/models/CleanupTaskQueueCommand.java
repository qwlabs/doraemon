package com.qwlabs.tq.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.annotation.Nullable;


import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class CleanupTaskQueueCommand {
    @Nullable
    private final String bucket;
    @NotNull
    @NotEmpty
    private final Set<@NotNull String> topics;
    @NotNull
    @NotEmpty
    private final Set<@NotNull ProcessStatus> statuses;
}
