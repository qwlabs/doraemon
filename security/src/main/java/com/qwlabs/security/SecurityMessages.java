package com.qwlabs.security;

import com.qwlabs.exceptions.CodeException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import javax.inject.Inject;

import static org.jboss.logging.Messages.getBundle;

@MessageBundle(projectCode = "STORAGE")
public interface SecurityMessages {
    @Inject
    SecurityMessages INSTANCE = getBundle(SecurityMessages.class);

    @Message(value = "Not found caller permission provider", format = Message.Format.MESSAGE_FORMAT)
    CodeException notFoundCallerPermissionProvider();
}
