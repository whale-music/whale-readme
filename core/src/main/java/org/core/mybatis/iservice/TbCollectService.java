package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbCollectPojo;

import java.util.Collection;
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
    
    /**
     * 分页获取用户歌单
     *
     * @param userId  用户 id
     * @param type    歌单类型
     * @param current 分页参数, 当前页数
     * @param size    每页大小
     * @return 返回分页数据
     */
    Page<TbCollectPojo> getUserCollect(Long userId, Collection<Byte> type, long current, long size);
}
