package org.oss.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.SaveConfig;
import org.oss.service.OSSService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OSSFactory {
    private static final Log log = LogFactory.get(OSSFactory.class);
    
    
    private static final Map<String, OSSService> map = new CaseInsensitiveMap<>();
    
    private static final String PACKAGE_NAME = "org.oss.service.impl";
    
    private static final String INTERFACE_NAME = "org.oss.service.OSSService";
    
    private OSSFactory() {
    }
    
    static {
        Set<Class<?>> classes = ClassUtil.scanPackage(PACKAGE_NAME);
        for (Class<?> aClass : classes) {
            try {
                List<Class<?>> interfaces = Arrays.asList(aClass.getInterfaces());
                List<String> classNameList = interfaces.stream().map(Class::getName).toList();
                if (classNameList.contains(INTERFACE_NAME)) {
                    Object o = aClass.getDeclaredConstructor().newInstance();
                    OSSService convert = Convert.convert(OSSService.class, o);
                    map.put(convert.getMode(), convert);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * 获取音乐处理工厂
     *
     * @param config 保存信息
     */
    public static OSSService ossFactory(SaveConfig config) {
        OSSService oss = map.get(config.getSaveMode());
        if (oss == null) {
            throw new BaseException(ResultCode.SAVE_NAME_INVALID);
        }
        oss.isConnected(config);
        return oss;
    }
}
