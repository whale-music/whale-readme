package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.core.mybatis.model.convert.UserConvert;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaveOrUpdateUserReq extends UserConvert {
    private String avatarTempFile;
    private String backgroundTempFile;
    
    public SaveOrUpdateUserReq(String avatarUrl, String backgroundPicUrl) {
        super(avatarUrl, backgroundPicUrl);
    }
    
    public SaveOrUpdateUserReq() {
    }
    
}
