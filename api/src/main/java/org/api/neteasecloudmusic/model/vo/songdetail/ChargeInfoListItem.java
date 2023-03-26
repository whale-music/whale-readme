package org.api.neteasecloudmusic.model.vo.songdetail;

import lombok.Data;

@Data
public class ChargeInfoListItem {
    private Integer rate;
    private Object chargeMessage;
    private Integer chargeType;
    private Object chargeUrl;
}