package com.qwlabs.storage.messages;


import com.qwlabs.exceptions.BadRequestException;
import com.qwlabs.exceptions.CodeException;
import com.qwlabs.exceptions.NotImplementedException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.annotations.ValidIdRange;

import java.lang.invoke.MethodHandles;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
@ValidIdRange(min = 2000, max = 2999)
public interface Messages {

    Messages INSTANCE = getBundle(MethodHandles.lookup(), Messages.class);

    @Message(id = 2000, value = "{0} does not support {1} operation", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException notSupported(String provider, String operation);

    @Message(id = 2001, value = "Unable to find the storage planner corresponding to {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException notFoundStoragePlanner(String businessType);

    @Message(id = 2002, value = "Unable to find the {0} attribute in the context", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException notFoundContextAttribute(String attributeName);

    @Message(id = 2003, value = "Property name in context: {0}, value: {1}, type is not {2}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidContextAttributeType(String attributeName, String value, String type);

    @Message(id = 2004, value = "Inconsistent number of part, expected: {0} Actual: {1}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidPartCount(int expected, int actual);

    @Message(id = 2005, value = "Unable to find storage service corresponding to: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException notFoundStorageService(String provider);

    @Message(id = 2006, value = "Unable to find: {0} configuration", format = Message.Format.MESSAGE_FORMAT)
    CodeException lostConfig(String config);

    @Message(id = 2007, value = "Get file exception: bucketName: {0} objectName: {1}: message: {2}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException failedToGetFile(String bucketName, String objectName, String message);

    @Message(id = 2008, value = "not implemented", format = Message.Format.MESSAGE_FORMAT)
    NotImplementedException notImplemented();

    @Message(id = 2009, value = "Invalid context: {0}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidContent(String context);
}
