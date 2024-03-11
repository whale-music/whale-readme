package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformConfigRes {
    private String version;
    private String title;
    private Boolean fixedHeader;
    private Boolean hiddenSideBar;
    private Boolean multiTagsCache;
    private Boolean keepAlive;
    private String locale;
    private String layout;
    private String theme;
    private Boolean darkMode;
    private String overallStyle;
    private Boolean grey;
    private Boolean weak;
    private Boolean hideTabs;
    private Boolean hideFooter;
    private Boolean sidebarStatus;
    private String epThemeColor;
    private Boolean showLogo;
    private String showModel;
    private Boolean menuArrowIconNoTransition;
    private Boolean cachingAsyncRoutes;
    private String tooltipEffect;
    private String responsiveStorageNameSpace;
    private int menuSearchHistory;
    private MapConfigure mapConfigure;
    
    @Data
    public static class MapConfigure {
        private String amapKey;
        private Options options;
    }
    
    @Data
    public static class Options {
        private Boolean resizeEnable;
        private List<Double> center;
        private int zoom;
    }
}
