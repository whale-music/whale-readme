package org.web.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.PageTagsReq;
import org.api.admin.model.res.PageTagsRes;
import org.api.admin.service.TagsApi;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.springframework.web.bind.annotation.*;

@RestController(AdminConfig.ADMIN + "TagsController")
@RequestMapping("/admin/tags")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class TagsController {
    private final TagsApi tagsApi;
    
    @WebLog
    @PostMapping("/page")
    public R getPageTags(@RequestBody PageTagsReq req) {
        final PageResCommon<PageTagsRes> res = tagsApi.getPageTags(req);
        return R.success(res);
    }
}
