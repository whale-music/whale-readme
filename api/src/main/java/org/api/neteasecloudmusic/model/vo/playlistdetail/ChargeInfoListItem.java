package org.api.neteasecloudmusic.model.vo.playlistdetail;

import lombok.Data;

@Data
public class ChargeInfoListItem {
    private Integer rate;
    private Object chargeMessage;
    private Integer chargeType;
    private Object chargeUrl;
}