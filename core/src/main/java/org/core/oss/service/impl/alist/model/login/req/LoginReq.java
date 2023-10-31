package org.core.oss.service.impl.alist.model.login.req;

public class LoginReq {
    private String password;
    private String otpCode;
    private String username;
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getOtpCode() {
        return otpCode;
    }
    
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String toString() {
        return
                "LoginReq{" +
                        "password = '" + password + '\'' +
                        ",otp_code = '" + otpCode + '\'' +
                        ",username = '" + username + '\'' +
                        "}";
    }
}
