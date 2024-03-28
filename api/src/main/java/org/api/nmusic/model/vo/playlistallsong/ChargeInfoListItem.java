package org.api.nmusic.model.vo.playlistallsong;

import lombok.Data;

@Data
public class ChargeInfoListItem {
    private Integer rate;
    private Object chargeMessage;
    private Integer chargeType;
    private Object chargeUrl;
}