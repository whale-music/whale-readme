package org.core.service;

import org.core.common.weblog.model.WebLogRecord;

public interface SysLogWriteService {
    /**
     * 写入controller请求日志记录, 该方法是异步的
     *
     * @param webLogRecord 日志信息
     */
    void writeWebLogRecord(WebLogRecord webLogRecord);
}
