package org.api.neteasecloudmusic.model.vo.songurl;

import lombok.Data;

@Data
public class FreeTrialPrivilege {
    private boolean userConsumable;
    private boolean resConsumable;
    private Object cannotListenReason;
    private Object listenType;
}