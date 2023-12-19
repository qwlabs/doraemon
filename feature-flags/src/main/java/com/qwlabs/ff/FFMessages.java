package com.qwlabs.ff;


import com.qwlabs.exceptions.CodeException;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
public interface FFMessages {
    int BASE = 3000;
    @Inject
    FFMessages INSTANCE = getBundle(FFMessages.class);

    @Message(id = BASE, value = "Can not found feature: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException featureNotFound(String feature);
}
