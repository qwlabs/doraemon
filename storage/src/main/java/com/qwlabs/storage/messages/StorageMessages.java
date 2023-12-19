package com.qwlabs.storage.messages;


import com.qwlabs.exceptions.BadRequestException;
import com.qwlabs.exceptions.CodeException;
import com.qwlabs.exceptions.NotImplementedException;
import jakarta.inject.Inject;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import static org.jboss.logging.Messages.getBundle;

@SuppressWarnings("checkstyle:MagicNumber")
@MessageBundle(projectCode = "DORA")
public interface StorageMessages {
    int BASE = 2000;

    @Inject
    StorageMessages INSTANCE = getBundle(StorageMessages.class);

    @Message(id = BASE, value = "{0} does not support {1} operation", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException notSupported(String provider, String operation);

    @Message(id = BASE + 1, value = "Unable to find the storage planner corresponding to {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException notFoundStoragePlanner(String businessType);

    @Message(id = BASE + 2, value = "Unable to find the {0} attribute in the context", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException notFoundContextAttribute(String attributeName);

    @Message(id = BASE + 3, value = "Property name in context: {0}, value: {1}, type is not {2}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidContextAttributeType(String attributeName, String value, String type);

    @Message(id = BASE + 4, value = "Inconsistent number of part, expected: {0} Actual: {1}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidPartCount(int expected, int actual);

    @Message(id = BASE + 5, value = "Unable to find storage service corresponding to: {0}", format = Message.Format.MESSAGE_FORMAT)
    CodeException notFoundStorageService(String provider);

    @Message(id = BASE + 6, value = "Unable to find: {0} configuration", format = Message.Format.MESSAGE_FORMAT)
    CodeException lostConfig(String config);

    @Message(id = BASE + 7, value = "Get file exception: bucketName: {0} objectName: {1}: message: {2}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException failedToGetFile(String bucketName, String objectName, String message);

    @Message(id = BASE + 8, value = "not implemented", format = Message.Format.MESSAGE_FORMAT)
    NotImplementedException notImplemented();

    @Message(id = BASE + 9, value = "Invalid context: {0}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidContent(String context);
}
