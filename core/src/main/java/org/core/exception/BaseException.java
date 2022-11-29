package org.core.exception;

import org.core.common.exception.BaseErrorInfoInterface;

/**
 * &#064;Deprecated  异常类
 *
 * @author Sakura
 */
public class BaseException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private final String errorCode;
    /**
     * 错误信息
     */
    private final String errorMsg;
    
    public BaseException() {
        super();
        this.errorCode = null;
        this.errorMsg = null;
    }
    
    public BaseException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getCode());
        this.errorCode = errorInfoInterface.getCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }
    
    public BaseException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getCode(), cause);
        this.errorCode = errorInfoInterface.getCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }
    
    public BaseException(String errorMsg) {
        super(errorMsg);
        this.errorCode = null;
        this.errorMsg = errorMsg;
    }
    
    public BaseException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public BaseException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    
    public String getErrorCode() {
        return errorCode;
    }
    
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
}
