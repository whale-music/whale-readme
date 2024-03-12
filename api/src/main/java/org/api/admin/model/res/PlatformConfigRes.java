package org.api.admin.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlatformConfigRes {
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("FixedHeader")
    private Boolean fixedHeader;
    @JsonProperty("HiddenSideBar")
    private Boolean hiddenSideBar;
    @JsonProperty("MultiTagsCache")
    private Boolean multiTagsCache;
    @JsonProperty("KeepAlive")
    private Boolean keepAlive;
    @JsonProperty("Locale")
    private String locale;
    @JsonProperty("Layout")
    private String layout;
    @JsonProperty("Theme")
    private String theme;
    @JsonProperty("DarkMode")
    private Boolean darkMode;
    @JsonProperty("OverallStyle")
    private String overallStyle;
    @JsonProperty("Grey")
    private Boolean grey;
    @JsonProperty("Weak")
    private Boolean weak;
    @JsonProperty("HideTabs")
    private Boolean hideTabs;
    @JsonProperty("HideFooter")
    private Boolean hideFooter;
    @JsonProperty("SidebarStatus")
    private Boolean sidebarStatus;
    @JsonProperty("EpThemeColor")
    private String epThemeColor;
    @JsonProperty("ShowLogo")
    private Boolean showLogo;
    @JsonProperty("ShowModel")
    private String showModel;
    @JsonProperty("MenuArrowIconNoTransition")
    private Boolean menuArrowIconNoTransition;
    @JsonProperty("CachingAsyncRoutes")
    private Boolean cachingAsyncRoutes;
    @JsonProperty("TooltipEffect")
    private String tooltipEffect;
    @JsonProperty("ResponsiveStorageNameSpace")
    private String responsiveStorageNameSpace;
    @JsonProperty("MenuSearchHistory")
    private int menuSearchHistory;
    @JsonProperty("MapConfigure")
    private MapConfigure mapConfigure;

    @Data
    public static class MapConfigure {
        @JsonProperty("amapKey")
        private String amapKey;
        @JsonProperty("options")
        private Options options;
    }

    @Data
    public static class Options {
        @JsonProperty("resizeEnable")
        private Boolean resizeEnable;
        @JsonProperty("center")
        private List<Double> center;
        @JsonProperty("zoom")
        private int zoom;
    }
}
