package org.web.nmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.playlist.PlayListVo;
import org.api.nmusic.model.vo.subcount.Subcount;
import org.api.nmusic.model.vo.user.UserVo;
import org.api.nmusic.model.vo.user.detail.UserDetailRes;
import org.api.nmusic.model.vo.user.record.UserRecordRes;
import org.api.nmusic.service.UserApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.UserUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web.nmusic.controller.BaseController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * NMusicApi 用户控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController(NeteaseCloudConfig.NETEASECLOUD + "UserController")
@RequestMapping("/")
@Slf4j
public class UserController extends BaseController {
    
    private final UserApi user;
    
    public UserController(UserApi user) {
        this.user = user;
    }
    
    
    /**
     * 获取用户信息
     *
     * @return 返回用户信息
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/user/account", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult getUser() {
        SysUserPojo userPojo = UserUtil.getUser();
        // 查找用户
        UserConvert account = user.getAccount(userPojo.getId());
        UserVo userVo = getUserVo(account);
        
        // 前端通用返回类
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
    /**
     * 初始化用户昵称
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/activate/init/profile", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult initUser(@RequestParam("nickname") String nickname) {
        SysUserPojo userPojo = UserUtil.getUser();
        userPojo.setNickname(nickname);
        user.updateUserPojo(userPojo);
        return new NeteaseResult().success();
    }
    
    /**
     * 用户歌单
     *
     * @param uid 用户ID
     * @return 返回用户歌单
     */
    @AnonymousAccess
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/user/playlist", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult userPlayList(@RequestParam(value = "uid", required = false) Long uid, @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Long pageIndex, @RequestParam(value = "pageSize", required = false, defaultValue = "30") Long pageSize) {
        uid = Optional.ofNullable(uid).orElse(UserUtil.getUser().getId());
        // 如果歌单查询没有值，直接返回
        PlayListVo playList = user.getPlayList(uid, pageIndex, pageSize);
    
        NeteaseResult neteaseResult = new NeteaseResult();
        neteaseResult.putAll(BeanUtil.beanToMap(playList));
        return neteaseResult.success();
    }
    
    /**
     * 获取用户收藏，创建
     * 歌单，收藏，mv, dj 数量
     *
     * @return 计数
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/user/subcount", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult getSubcount() {
        SysUserPojo userPojo = UserUtil.getUser();
        
        NeteaseResult r = new NeteaseResult();
        Subcount subcount = new Subcount();
        // 收藏歌单
        subcount.setSubPlaylistCount(user.getSubPlaylistCount(userPojo.getId()));
        // 创建歌单
        subcount.setCreatedPlaylistCount(user.getCreatedPlaylistCount(userPojo.getId()));
        // 关注歌手
        subcount.setArtistCount(user.getUserBySinger(userPojo.getId()));
        // 首次从Dj
        subcount.setDjRadioCount(0L);
        // 创建DJ
        subcount.setCreateDjRadioCount(0L);
        // MV
        subcount.setMvCount(0L);
        r.putAll(BeanUtil.beanToMap(subcount));
        return r.success();
    }
    
    
    /**
     * 获取用户听歌历史记录
     *
     * @param uid 用户ID
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/user/record", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult userRecord(@RequestParam(value = "uid", required = false) Long uid, @RequestParam(value = "type", required = false, defaultValue = "0") Long type) {
        Long userId = Objects.isNull(uid) ? UserUtil.getUser().getId() : uid;
        List<UserRecordRes> res = user.userRecord(userId, type);
        NeteaseResult r = new NeteaseResult();
        if (type == 1) {
            r.put("weekData", res);
        } else if (type == 0) {
            r.put("allData", res);
        }
        return r.success();
    }
    
    @AnonymousAccess
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/user/detail", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult userDetail(@RequestParam("uid") Long uid) {
        UserDetailRes res = user.userDetail(uid);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
}
