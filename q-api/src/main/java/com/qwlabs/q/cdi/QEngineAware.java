package com.qwlabs.q.cdi;

import com.qwlabs.cdi.SafeCDI;
import com.qwlabs.q.QMessages;
import com.qwlabs.q.QEngine;

import jakarta.enterprise.inject.Instance;

public interface QEngineAware {
    default QEngine engine() {
        return SafeCDI.select(QEngine.class)
                .map(Instance::get)
                .orElseThrow(QMessages.INSTANCE::engineNotFound);
    }
}
