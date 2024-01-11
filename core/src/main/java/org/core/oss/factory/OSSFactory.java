package org.core.oss.factory;

import cn.hutool.core.map.CaseInsensitiveMap;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.oss.service.OSSService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OSSFactory {
    
    /**
     * 存储OSS，Map key是忽略大小写的
     */
    private final Map<String, OSSService> ossMap;
    
    private final SaveConfig saveConfig;
    
    public OSSFactory(Map<String, OSSService> ossMap, SaveConfig saveConfig) {
        this.ossMap = new CaseInsensitiveMap<>(ossMap);
        this.saveConfig = saveConfig;
    }
    
    /**
     * 获取音乐处理工厂
     */
    public OSSService ossFactory() {
        OSSService oss = ossMap.get(saveConfig.getSaveMode());
        if (oss == null) {
            throw new BaseException(ResultCode.SAVE_NAME_INVALID);
        }
        oss.isConnected(saveConfig);
        return oss;
    }
}
