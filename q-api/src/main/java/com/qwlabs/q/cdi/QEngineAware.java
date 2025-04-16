package com.qwlabs.q.cdi;

import com.qwlabs.cdi.CDI2;
import com.qwlabs.q.Messages;
import com.qwlabs.q.QEngine;
import jakarta.enterprise.inject.Instance;

public interface QEngineAware {
    default QEngine engine() {
        return CDI2.select(QEngine.class)
            .map(Instance::get)
            .orElseThrow(Messages.INSTANCE::engineNotFound);
    }
}
