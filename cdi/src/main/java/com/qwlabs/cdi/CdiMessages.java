package com.qwlabs.cdi;

import com.qwlabs.exceptions.CodeException;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import static org.jboss.logging.Messages.getBundle;

@MessageBundle(projectCode = "CDI")
public interface CdiMessages {
    @Inject
    CdiMessages INSTANCE = getBundle(CdiMessages.class);
    @Message(value = "代码错误: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException codeError(String message);
}
