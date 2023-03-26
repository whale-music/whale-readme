package org.api.neteasecloudmusic.model.vo.user.detail;

import lombok.Data;

@Data
public class BindingsItem {
    private int expiresIn;
    private boolean expired;
    private Object tokenJsonStr;
    private int refreshTime;
    private int id;
    private int type;
    private int userId;
    private long bindingTime;
    private String url;
}