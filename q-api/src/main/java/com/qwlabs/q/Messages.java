package com.qwlabs.q;

import com.qwlabs.exceptions.BadRequestException;
import com.qwlabs.exceptions.CodeException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.annotations.ValidIdRange;

import java.lang.invoke.MethodHandles;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
@ValidIdRange(min = 1000, max = 1999)
public interface Messages {

    Messages INSTANCE = getBundle(MethodHandles.lookup(), Messages.class);

    @Message(id = 1000, value = "Invalid dialect: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException invalidDialect(String dialect);

    @Message(id = 1001, value = "Invalid query: {0}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidQuery(String query);

    @Message(id = 1002, value = "Q Engine not found", format = Message.Format.MESSAGE_FORMAT)
    CodeException engineNotFound();
}
