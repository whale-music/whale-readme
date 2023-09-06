package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbCollectPojo;

import java.util.List;

/**
 * <p>
 * 歌单列表 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbCollectService extends IService<TbCollectPojo> {
    
    /**
     * 获取用户所有歌单
     *
     * @param userId 用户ID
     * @param type   歌单类型
     * @return 歌单列表
     */
    List<TbCollectPojo> getUserCollect(Long userId, byte type);
}
