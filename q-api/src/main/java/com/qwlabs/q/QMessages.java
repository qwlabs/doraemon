package com.qwlabs.q;

import com.qwlabs.exceptions.BadRequestException;
import com.qwlabs.exceptions.CodeException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import javax.inject.Inject;

import static org.jboss.logging.Messages.getBundle;

@MessageBundle(projectCode = "Q")
public interface QMessages {
    @Inject
    QMessages INSTANCE = getBundle(QMessages.class);

    @Message(value = "Invalid dialect: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException invalidDialect(String dialect);

    @Message(value = "Invalid query: {0}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidQuery(String query);

    @Message(value = "Q Engine not found", format = Message.Format.MESSAGE_FORMAT)
    CodeException engineNotFound();
}
