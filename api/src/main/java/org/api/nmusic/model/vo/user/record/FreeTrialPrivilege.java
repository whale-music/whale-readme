package org.api.nmusic.model.vo.user.record;

import lombok.Data;

@Data
public class FreeTrialPrivilege{
	private boolean userConsumable;
	private boolean resConsumable;
	private Object listenType;
}