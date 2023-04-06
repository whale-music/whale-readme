package org.oss.service.impl.alist.model.login.res;

public class LoginRes {
	private int code;
	private DataRes data;
	private String message;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public DataRes getData() {
		return data;
	}
	
	public void setData(DataRes data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return
				"LoginRes{" +
						"code = '" + code + '\'' +
						",data = '" + data + '\'' +
						",message = '" + message + '\'' +
						"}";
	}
}
