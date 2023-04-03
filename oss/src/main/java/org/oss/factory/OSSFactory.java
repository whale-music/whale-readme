package org.oss.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.oss.service.OSSService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OSSFactory {
    private static final Log log = LogFactory.get(OSSFactory.class);
    
    
    private static final HashMap<String, OSSService> map = new HashMap<>();
    
    private static final String PACKAGE_NAME = "org.oss.service.impl";
    
    private static final String INTERFACE_NAME = "org.oss.service.OSSService";
    
    static {
        Set<Class<?>> classes = ClassUtil.scanPackage(PACKAGE_NAME);
        for (Class<?> aClass : classes) {
            try {
                List<Class<?>> interfaces = Arrays.asList(aClass.getInterfaces());
                List<String> classNameList = interfaces.stream().map(Class::getName).collect(Collectors.toList());
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
    
    private OSSFactory() {
    }
    
    /**
     * 获取音乐处理工厂
     *
     * @param serviceName 音乐地址处理
     */
    public static OSSService ossFactory(String serviceName) {
        OSSService oss = map.get(serviceName);
        if (oss == null) {
            throw new BaseException(ResultCode.SAVE_NAME_INVALID);
        }
        return oss;
    }
}
