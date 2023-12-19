package com.qwlabs.q;

import com.qwlabs.exceptions.BadRequestException;
import com.qwlabs.exceptions.CodeException;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import static org.jboss.logging.Messages.getBundle;


@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
public interface QMessages {
    int BASE = 1000;
    @Inject
    QMessages INSTANCE = getBundle(QMessages.class);

    @Message(id = BASE, value = "Invalid dialect: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException invalidDialect(String dialect);

    @Message(id = BASE + 1, value = "Invalid query: {0}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidQuery(String query);

    @Message(id = BASE + 2, value = "Q Engine not found", format = Message.Format.MESSAGE_FORMAT)
    CodeException engineNotFound();
}
