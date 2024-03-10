package org.web.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.DeleteHistoryReq;
import org.api.admin.model.res.MusicHistoryRes;
import org.api.admin.service.HistoryApi;
import org.core.common.result.R;
import org.springframework.web.bind.annotation.*;

@RestController(AdminConfig.ADMIN + "HistoryController")
@RequestMapping("/admin/history")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class HistoryController {
    
    private final HistoryApi historyApi;
    
    /**
     * @param name    用户昵称查询
     * @param current 分页参数 从1开始
     * @param size    分页大小
     * @return 历史数据
     */
    @GetMapping("/page")
    public R getPageHistory(@RequestParam("name") String name,
                            @RequestParam(value = "current", defaultValue = "1", required = false) Integer current,
                            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size
    ) {
        PageResCommon<MusicHistoryRes> res = historyApi.getPageHistory(name, current, size);
        return R.success(res);
    }
    
    @DeleteMapping("/")
    public R deletePageHistory(@RequestBody DeleteHistoryReq req) {
        historyApi.deletePageHistory(req.getIds());
        return R.success();
    }
}
