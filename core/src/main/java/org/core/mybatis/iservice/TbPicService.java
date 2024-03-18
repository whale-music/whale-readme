package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbPicPojo;

import java.util.List;

/**
 * <p>
 * 音乐专辑歌单封面表 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbPicService extends IService<TbPicPojo> {
    
    /**
     * 获取封面信息
     *
     * @param path 封面路径
     * @return 封面信息
     */
    TbPicPojo getPicResourceByPath(String path);
    
    /**
     * 获取封面数据
     *
     * @param paths 封面路径
     * @return 封面数据
     */
    List<TbPicPojo> getResourceByPath(List<String> paths);
}
