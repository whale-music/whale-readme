package org.api.nmusic.model.vo.playlistallsong;

import lombok.Data;

@Data
public class FreeTrialPrivilege {
    private Boolean userConsumable;
    private Boolean resConsumable;
    private Object listenType;
}