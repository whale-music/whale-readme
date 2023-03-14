package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.personalfm.PersonalFMRes;
import org.api.neteasecloudmusic.model.vo.personalized.PersonalizedRes;
import org.api.neteasecloudmusic.service.RecommendApi;
import org.core.common.result.NeteaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * NeteaseCloudMusicApi 推荐控制器
 * </p>
 *
 * @author Sakura
 * @since 2023-03-13
 */
@RestController("NeteaseCloudRecommend")
@RequestMapping("/")
@Slf4j
public class RecommendController {
    
    @Autowired
    private RecommendApi recommendApi;
    
    /**
     * 推荐FM
     */
    @GetMapping("/personal_fm")
    public NeteaseResult personalFM() {
        PersonalFMRes personalFM = recommendApi.personalFM();
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(personalFM));
        return r.success();
    }
    
    
    /**
     * 推荐歌单
     *
     * @param limit 取出数量 , 默认为 30 (不支持 offset)
     */
    @GetMapping("/personalized")
    public NeteaseResult personalized(@RequestParam(value = "limit", required = false, defaultValue = "30") Long limit) {
        PersonalizedRes res = recommendApi.personalized(limit);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
}
