package org.api.nmusic.model.vo.album.album;

import lombok.Data;

@Data
public class ChargeInfoListItem{
	private int rate;
	private Object chargeMessage;
	private int chargeType;
	private Object chargeUrl;
}