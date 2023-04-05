package org.api.model.playlistdetail;

import lombok.Data;

@Data
public class ChargeInfoListItem {
    private Integer rate;
    private Object chargeMessage;
    private Integer chargeType;
    private Object chargeUrl;
}