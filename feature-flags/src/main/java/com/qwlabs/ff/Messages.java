package com.qwlabs.ff;


import com.qwlabs.exceptions.CodeException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.annotations.ValidIdRange;

import java.lang.invoke.MethodHandles;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
@ValidIdRange(min = 3000, max = 3999)
public interface Messages {

    Messages INSTANCE = getBundle(MethodHandles.lookup(), Messages.class);

    @Message(id = 3000, value = "Can not found feature: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException featureNotFound(String feature);
}
