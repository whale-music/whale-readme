package org.api.nmusic.model.vo.album.album;

import lombok.Data;

@Data
public class FreeTrialPrivilege{
	private boolean userConsumable;
	private boolean resConsumable;
	private Object listenType;
}