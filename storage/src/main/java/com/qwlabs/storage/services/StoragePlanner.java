package com.qwlabs.storage.services;

import com.qwlabs.storage.models.StoragePlan;

import javax.validation.constraints.NotNull;

public interface StoragePlanner {
    StoragePlan plan(@NotNull StorageContext context);
}
