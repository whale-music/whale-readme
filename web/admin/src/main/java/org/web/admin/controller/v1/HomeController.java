package org.web.admin.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.convert.Count;
import org.api.admin.model.res.UsersUploadRes;
import org.api.admin.service.HoneApi;
import org.core.common.result.R;
import org.core.utils.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController(AdminConfig.ADMIN + "HomeController")
@RequestMapping("/admin/home")
@Slf4j
public class HomeController {
    
    private final HoneApi honeApi;
    
    @GetMapping("/count")
    @Operation(summary = "获取数据库统计")
    public R getCount() {
        Count musicCount = honeApi.getMusicCount();
        Count albumCount = honeApi.getAlbumCount();
        Count artistCount = honeApi.getArtistCount();
        Count mvCount = honeApi.getMvCount();
        HashMap<String, Count> map = new HashMap<>();
        map.put("music", musicCount);
        map.put("album", albumCount);
        map.put("artist", artistCount);
        map.put("mv", mvCount);
        return R.success(map);
    }
    
    @GetMapping("/users/upload")
    public R getUsersUpload() {
        List<UsersUploadRes> hone = honeApi.getUsersUpload();
        return R.success(hone);
    }
    
    @GetMapping("/music/statistics")
    public R getMusicStatistics() {
        return R.success(honeApi.getMusicStatistics());
    }
    
    @GetMapping("/music/task")
    public R getPluginTask(@RequestParam(value = "id", required = false) Long id) {
        id = Optional.ofNullable(id).orElse(UserUtil.getUser().getId());
        return R.success(honeApi.getPluginTask(id));
    }
}
