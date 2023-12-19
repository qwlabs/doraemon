package com.qwlabs.cdi;

import com.qwlabs.exceptions.CodeException;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
public interface CDIMessages {
    int BASE = 0;
    @Inject
    CDIMessages INSTANCE = getBundle(CDIMessages.class);

    @Message(id = BASE + 1, value = "Code error: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException codeError(String message);
}
