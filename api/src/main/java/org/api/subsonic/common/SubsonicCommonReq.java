package org.api.subsonic.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubsonicCommonReq {
    /**
     * 用户名
     */
    private String u;
    
    /**
     * 密码，可以是明文形式，也可以是带有“enc：”前缀的十六进制编码。从1.13.0开始，这应仅用于测试目的。
     */
    private String p;
    
    /**
     * （自1.13.0起）身份验证令牌计算为md5（password + salt）。详情见下文。
     */
    private String t;
    
    /**
     * （自1.13.0起）一个随机字符串（“salt”），用作计算密码哈希的输入。详情见下文。
     */
    private String s;
    
    /**
     * 客户端实现的协议版本，即，所使用的subsonic-rest-API.xsd模式的版本（见下文）。
     */
    private String v;
    
    /**
     * 标识客户端应用程序的唯一字符串。
     */
    private String c;
    
    /**
     * 请求以这种格式返回数据。支持的值是“xml”、“json”（从1.4.0开始）和“jsonp”（从1.6.0开始）。如果使用jsonp，请使用 callback 参数指定javascript回调函数的名称。
     */
    private String f;
}
