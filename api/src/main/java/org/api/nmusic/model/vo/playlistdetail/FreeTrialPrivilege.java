package org.api.nmusic.model.vo.playlistdetail;

import lombok.Data;

@Data
public class FreeTrialPrivilege {
    private boolean userConsumable;
    private boolean resConsumable;
    private Object listenType;
}