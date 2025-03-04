package com.qwlabs.excel;

import com.qwlabs.exceptions.CodeException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.annotations.ValidIdRange;

import java.lang.invoke.MethodHandles;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
@ValidIdRange(min = 4000, max = 4999)
public interface Messages {

    Messages INSTANCE = getBundle(MethodHandles.lookup(), Messages.class);

    @Message(id = 4000, value = "Conflict between head row start index {0} and data start row {1}", format = Message.Format.MESSAGE_FORMAT)
    CodeException conflictHeadAndDataRowIndex(int headStartRowIndex, int dataStartRowIndex);
}
