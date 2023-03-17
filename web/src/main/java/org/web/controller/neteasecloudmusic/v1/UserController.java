package org.web.controller.neteasecloudmusic.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.playlist.PlayListVo;
import org.api.neteasecloudmusic.model.vo.subcount.Subcount;
import org.api.neteasecloudmusic.model.vo.user.UserVo;
import org.api.neteasecloudmusic.model.vo.user.record.UserRecordRes;
import org.api.neteasecloudmusic.service.CollectApi;
import org.api.neteasecloudmusic.service.UserApi;
import org.core.common.result.NeteaseResult;
import org.core.pojo.SysUserPojo;
import org.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web.controller.BaseController;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * NeteaseCloudMusicApi 用户控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController("NeteaseCloudUser")
@RequestMapping("/")
@Slf4j
public class UserController extends BaseController {
    
    @Autowired
    private UserApi user;
    
    @Autowired
    private CollectApi collect;
    
    /**
     * 获取用户信息
     *
     * @return 返回用户信息
     */
    @GetMapping("/user/account")
    public NeteaseResult getUser() {
        SysUserPojo userPojo = UserUtil.getUser();
        // 查找用户
        SysUserPojo account = user.getAccount(userPojo.getId());
        UserVo userVo = getUserVo(account);
        
        // 前端通用返回类
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
    /**
     * 初始化用户昵称
     */
    @GetMapping("/activate/init/profile")
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
    @GetMapping("/user/playlist")
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
    @GetMapping("/user/subcount")
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
    
    
    
    @GetMapping("/user/record")
    public NeteaseResult userRecord(@RequestParam("uid") Long uid, @RequestParam(value = "type", required = false, defaultValue = "0") Long type) {
        List<UserRecordRes> res =  user.userRecord(uid,type);
        NeteaseResult r = new NeteaseResult();
        if (type == 1) {
            r.put("weekData", res);
        } else if (type == 0) {
            r.put("allData", res);
        }
        return r.success();
    }
}
