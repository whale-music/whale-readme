package org.web.admin.controller;

import org.api.admin.model.res.PlatformConfigRes;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.constant.WebMappingConstant;
import org.core.common.weblog.annotation.WebLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RootController {
    
    private final WebMappingConstant webMappingConstant;
    
    public RootController(WebMappingConstant webMappingConstant) {
        this.webMappingConstant = webMappingConstant;
    }
    
    @AnonymousAccess
    @WebLog
    @GetMapping("/")
    public ModelAndView web() {
        return new ModelAndView(webMappingConstant.getPath());
    }
    
    @AnonymousAccess
    @WebLog
    @GetMapping("/web/platform-config.json")
    public PlatformConfigRes platformConfig() {
        PlatformConfigRes platformConfigRes = new PlatformConfigRes();
        // 设置版本号
        platformConfigRes.setVersion("5.1.0");
        // 设置标题
        platformConfigRes.setTitle("Whale");
        // 设置是否固定头部
        platformConfigRes.setFixedHeader(true);
        // 设置是否隐藏侧边栏
        platformConfigRes.setHiddenSideBar(false);
        // 设置是否多标签缓存
        platformConfigRes.setMultiTagsCache(false);
        // 设置是否保持活动
        platformConfigRes.setKeepAlive(true);
        // 设置区域设置
        platformConfigRes.setLocale("zh");
        // 设置布局
        platformConfigRes.setLayout("vertical");
        // 设置主题
        platformConfigRes.setTheme("light");
        // 设置是否暗黑模式
        platformConfigRes.setDarkMode(false);
        // 设置总体风格
        platformConfigRes.setOverallStyle("light");
        // 设置灰色
        platformConfigRes.setGrey(false);
        // 设置弱
        platformConfigRes.setWeak(false);
        // 设置是否隐藏选项卡
        platformConfigRes.setHideTabs(true);
        // 设置是否隐藏页脚
        platformConfigRes.setHideFooter(false);
        // 设置侧边栏状态
        platformConfigRes.setSidebarStatus(true);
        // 设置主题色
        platformConfigRes.setEpThemeColor("#5352ed");
        // 设置是否显示logo
        platformConfigRes.setShowLogo(true);
        // 设置显示模式
        platformConfigRes.setShowModel("smart");
        // 设置菜单箭头图标是否无过渡
        platformConfigRes.setMenuArrowIconNoTransition(false);
        // 设置是否缓存异步路由
        platformConfigRes.setCachingAsyncRoutes(false);
        // 设置工具提示效果
        platformConfigRes.setTooltipEffect("light");
        // 设置响应式存储空间命名空间
        platformConfigRes.setResponsiveStorageNameSpace("responsive-");
        // 设置菜单搜索历史
        platformConfigRes.setMenuSearchHistory(6);
        
        // 设置地图配置
        // PlatformConfigRes.MapConfigure mapConfigure = new PlatformConfigRes.MapConfigure();
        // 设置高德地图密钥
        // mapConfigure.setAmapKey("97b3248d1553172e81f168cf94ea667e");
        
        // PlatformConfigRes.Options options = new PlatformConfigRes.Options();
        // 设置是否启用重新调整大小
        // options.setResizeEnable(true);
        // 设置中心点坐标
        // options.setCenter(List.of(113.6401, 34.72468));
        // 设置缩放级别
        // options.setZoom(12);
        
        // mapConfigure.setOptions(options);
        
        // 设置地图配置
        // platformConfigRes.setMapConfigure(mapConfigure);
        return platformConfigRes;
    }
}
