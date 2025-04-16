package com.qwlabs.storage.services;

import com.qwlabs.cdi.Primary;
import com.qwlabs.cdi.DispatchInstance;
import com.qwlabs.storage.messages.Messages;
import com.qwlabs.storage.models.StoragePlan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.Objects;

@Primary
@ApplicationScoped
public class StoragePlannerDispatcher implements StoragePlanner {
    private final DispatchInstance<StorageContext, StoragePlanner> dispatcher;

    @Inject
    public StoragePlannerDispatcher(Instance<StoragePlanner> instance) {
        this.dispatcher = DispatchInstance.of(instance);
    }

    @Override
    public StoragePlan plan(StorageContext context) {
        Objects.requireNonNull(context, "context can not be null.");
        String businessType = context.getBusinessType();
        Objects.requireNonNull(businessType, "context can not be null.");
        return this.dispatcher.getOptional(context)
            .map(planner -> planner.plan(context))
            .orElseThrow(() -> Messages.INSTANCE.notFoundStoragePlanner(businessType));
    }

}
