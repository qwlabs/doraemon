package com.qwlabs.excel.messages;


import com.qwlabs.excel.exceptions.ExcelReadException;
import com.qwlabs.excel.read.ReadMode;
import com.qwlabs.exceptions.CodeException;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import static org.jboss.logging.Messages.getBundle;

@MessageBundle(projectCode = "EXCEL")
public interface ExcelMessages {

    @Inject
    ExcelMessages INSTANCE = getBundle(ExcelMessages.class);

    @Message(value = "找不到编号为{0}的Sheet", format = Message.Format.MESSAGE_FORMAT)
    ExcelReadException sheetNoNotFound(int sheetNo);

    @Message(value = "找不到名称为{0}的Sheet", format = Message.Format.MESSAGE_FORMAT)
    ExcelReadException sheetNameNotFound(String sheetName);

    @Message(value = "找不到名称为{0}的DataReader", format = Message.Format.MESSAGE_FORMAT)
    CodeException dataReaderNotFound(ReadMode readMode);
}
