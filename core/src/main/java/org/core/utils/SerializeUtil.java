package org.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.common.exception.BaseException;
import org.common.result.ResultCode;

public class SerializeUtil {
    private SerializeUtil(){}
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final XmlMapper xmlMapper = new XmlMapper();
    
    static {
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
    }
    
    public static String json(Object result){
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new BaseException(ResultCode.SERIALIZATION_ERROR.getCode(), ResultCode.SERIALIZATION_ERROR.getResultMsg() + e.getMessage());
        }
    }
    
    public static String xml(Object result) {
        try {
            return xmlMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new BaseException(ResultCode.SERIALIZATION_ERROR.getCode(), ResultCode.SERIALIZATION_ERROR.getResultMsg() + e.getMessage());
        }
    }
    
    public static String serialize(Object result, boolean isJson) {
        if (isJson) {
            return json(result);
        }else{
            return xml(result);
        }
    }
}
