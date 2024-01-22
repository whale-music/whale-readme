package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbResourcePojo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 存储地址 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbResourceService extends IService<TbResourcePojo> {
    
    /**
     * 获取音乐资源
     *
     * @param musicIds 音乐ID
     * @return 音乐地址
     */
    Map<Long, List<TbResourcePojo>> getResourceMap(Collection<Long> musicIds);
    
    /**
     * 根据文件名获取音乐数据
     *
     * @param path 文件名
     * @return 音乐数据
     */
    // TODO ByName重命名
    TbResourcePojo getResourceByName(String path);
}
