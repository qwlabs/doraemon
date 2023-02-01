package com.qwlabs.ff;


import com.qwlabs.exceptions.CodeException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import jakarta.inject.Inject;

import static org.jboss.logging.Messages.getBundle;

@MessageBundle(projectCode = "FEATURE-FLAGS")
public interface FeatureFlagsMessages {
    @Inject
    FeatureFlagsMessages INSTANCE = getBundle(FeatureFlagsMessages.class);

    @Message(value = "Can not found feature: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException featureNotFound(String feature);
}
