package com.qwlabs.storage.services;

import com.qwlabs.cdi.DispatchInstance;
import com.qwlabs.storage.messages.StorageMessages;
import com.qwlabs.storage.models.StoragePlan;
import com.qwlabs.storage.services.StorageContext;
import com.qwlabs.storage.services.StoragePlanner;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Objects;

@ApplicationScoped
public class StoragePlannerDispatcher implements StoragePlanner {
    private final DispatchInstance<StorageContext, StoragePlanner> dispatcher;

    @Inject
    public StoragePlannerDispatcher(Instance<StoragePlanner> instance) {
        this.dispatcher = DispatchInstance.ofCache(instance);
    }

    @Override
    public StoragePlan plan(StorageContext context) {
        Objects.requireNonNull(context, "context can not be null.");
        String businessType = context.getBusinessType();
        Objects.requireNonNull(businessType, "context can not be null.");
        return this.dispatcher.getOptional(context)
                .map(planner -> planner.plan(context))
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundStoragePlanner(businessType));
    }

}
