package com.qwlabs.cdi;

import com.qwlabs.exceptions.CodeException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.annotations.ValidIdRange;

import java.lang.invoke.MethodHandles;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
@ValidIdRange(min = 0, max = 999)
public interface Messages {

    Messages INSTANCE = getBundle(MethodHandles.lookup(), Messages.class);

    @Message(id = 1, value = "Code error: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException codeError(String message);
}
