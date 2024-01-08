package com.qwlabs.excel;

import com.qwlabs.exceptions.CodeException;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
public interface ExcelMessages {
    int BASE = 4000;
    @Inject
    ExcelMessages INSTANCE = getBundle(ExcelMessages.class);

    @Message(id = BASE + 1, value = "Conflict between head row start index {0} and data start row {1}", format = Message.Format.MESSAGE_FORMAT)
    CodeException conflictHeadAndDataRowIndex(int headStartRowIndex, int dataStartRowIndex);
}
