package org.plugin.service.sync.impl.music163.model;

import java.util.List;

public class LikePlay{
	private int code;
	private List<Integer> ids;
	private long checkPoint;
	
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public List<Integer> getIds() {
		return ids;
	}
	
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	
	public long getCheckPoint() {
		return checkPoint;
	}
	
	public void setCheckPoint(long checkPoint) {
		this.checkPoint = checkPoint;
	}
}