package org.api.admin.model.res;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.core.mybatis.model.convert.UserConvert;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaveOrUpdateUserRes extends UserConvert {
    public SaveOrUpdateUserRes(String avatarUrl, String backgroundPicUrl) {
        super(avatarUrl, backgroundPicUrl);
    }
    
    public SaveOrUpdateUserRes() {
    }
}
