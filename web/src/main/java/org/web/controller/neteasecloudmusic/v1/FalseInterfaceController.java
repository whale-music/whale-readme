package org.web.controller.neteasecloudmusic.v1;

import lombok.extern.slf4j.Slf4j;
import org.core.common.result.NeteaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController("NeteaseCloud" + "FalseInterfaceController")
@RequestMapping("/")
@Slf4j
public class FalseInterfaceController {
    
    @GetMapping("/mv/sublist")
    public NeteaseResult mvSublist() {
        NeteaseResult r = new NeteaseResult();
        r.put("data", Collections.emptyList());
        r.put("count", 0);
        r.put("hasMore", false);
        return r.success();
    }
    
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
    
    @GetMapping("/user/record")
    public NeteaseResult userRecord() {
        NeteaseResult r = new NeteaseResult();
        r.put("weekData", Collections.emptyList());
        return r.success();
    }
    
    @RequestMapping(value = "/daily_signin", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult dailySignin(String type) {
        NeteaseResult r = new NeteaseResult();
        r.put("point", 5);
        return r.success();
    }
}
