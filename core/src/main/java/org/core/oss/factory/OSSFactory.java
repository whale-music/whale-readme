package org.core.oss.factory;

import cn.hutool.core.map.CaseInsensitiveMap;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.oss.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OSSFactory {
    
    /**
     * 存储OSS，Map key是忽略大小写的
     */
    private static Map<String, OSSService> _map;
    
    private static SaveConfig saveConfig;
    
    private OSSFactory() {
    }
    
    /**
     * 获取音乐处理工厂
     *
     * @param config 保存信息
     */
    public static OSSService ossFactory(SaveConfig config) {
        OSSService oss = _map.get(config.getSaveMode());
        if (oss == null) {
            throw new BaseException(ResultCode.SAVE_NAME_INVALID);
        }
        oss.isConnected(saveConfig);
        return oss;
    }
    
    @Autowired
    public void setMapAndSaveConfig(Map<String, OSSService> map, SaveConfig saveConfig) {
        OSSFactory._map = new CaseInsensitiveMap<>(map);
        OSSFactory.saveConfig = saveConfig;
    }
}
