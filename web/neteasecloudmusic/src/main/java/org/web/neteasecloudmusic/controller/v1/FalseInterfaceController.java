package org.web.neteasecloudmusic.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController(NeteaseCloudConfig.NETEASECLOUD + "FalseInterfaceController")
@RequestMapping("/")
@Slf4j
public class FalseInterfaceController {
    
    @WebLog
    @GetMapping("/mv/sublist")
    public NeteaseResult mvSublist() {
        NeteaseResult r = new NeteaseResult();
        r.put("data", Collections.emptyList());
        r.put("count", 0);
        r.put("hasMore", false);
        return r.success();
    }
    
    @WebLog
    @GetMapping("/user/cloud")
    public NeteaseResult userCloud() {
        NeteaseResult r = new NeteaseResult();
        r.put("data", Collections.emptyList());
        r.put("count", 0);
        r.put("size", 10839997);
        r.put("maxSize", 429496729);
        r.put("upgradeSign", 0);
        r.put("hasMore", false);
        return r.success();
    }
    
    @WebLog
    @GetMapping("/album/detail/dynamic")
    public NeteaseResult albumDetailDynamic(@RequestParam("id") Long id) {
        NeteaseResult r = new NeteaseResult();
        r.put("onSale", false);
        r.put("albumGameInfo", null);
        // 评论
        r.put("commentCount", 402);
        // 喜欢计数
        r.put("likedCount", 0);
        // 分享
        r.put("shareCount", 134);
        r.put("isSub", false);
        r.put("subTime", 0);
        // 提交次数
        r.put("subCount", 2913);
        r.put("code", 200);
        return r.success();
    }
    
    @WebLog
    @RequestMapping(value = "/daily_signin", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult dailySignin(String type) {
        NeteaseResult r = new NeteaseResult();
        r.put("point", 5);
        return r.success();
    }
}
