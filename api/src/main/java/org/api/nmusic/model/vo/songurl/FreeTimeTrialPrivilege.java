package org.api.nmusic.model.vo.songurl;

import lombok.Data;

@Data
public class FreeTimeTrialPrivilege {
    private boolean userConsumable;
    private boolean resConsumable;
    private int remainTime;
    private int type;
}