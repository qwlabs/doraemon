package com.qwlabs.storage.messages;


import com.qwlabs.exceptions.BadRequestException;
import com.qwlabs.exceptions.CodeException;
import com.qwlabs.exceptions.NotImplementedException;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

import javax.inject.Inject;

import static org.jboss.logging.Messages.getBundle;

@MessageBundle(projectCode = "STORAGE")
public interface StorageMessages {

    @Inject
    StorageMessages INSTANCE = getBundle(StorageMessages.class);

    @Message(value = "{0}不支持{1}操作", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException notSupported(String provider, String operate);

    @Message(value = "找不到:{0}对应的存储规划器", format = Message.Format.MESSAGE_FORMAT)
    CodeException notFoundStoragePlanner(String businessType);

    @Message(value = "上下文中找不到:{0}属性", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException notFoundContextAttribute(String attributeName);

    @Message(value = "上下文中属性 名称:{0}, 值:{1} 类型不是{2}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidContextAttributeType(String attributeName, String value, String type);

    @Message(value = "分片数量不一致, 期待:{0} 实际:{1}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException invalidPartCount(int expected, int actual);

    @Message(value = "找不到:{0}对应的存储服务", format = Message.Format.MESSAGE_FORMAT)
    CodeException notFoundStorageService(String provider);

    @Message(value = "找不到:{0}配置", format = Message.Format.MESSAGE_FORMAT)
    CodeException lostConfig(String config);

    @Message(value = "获取文件异常: bucketName:{0} objectName:{1}:message:{2}", format = Message.Format.MESSAGE_FORMAT)
    BadRequestException failedToGetFile(String bucketName, String objectName, String message);

    @Message(value = "未实现", format = Message.Format.MESSAGE_FORMAT)
    NotImplementedException notImplemented();
}
