package org.musicbox.controller.neteasecloudmusicapi.v1;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.musicbox.common.result.NeteaseResult;
import org.musicbox.common.vo.neteasecloudmusic.playlist.Creator;
import org.musicbox.common.vo.neteasecloudmusic.playlist.PlayListVo;
import org.musicbox.common.vo.neteasecloudmusic.playlist.PlaylistItem;
import org.musicbox.common.vo.neteasecloudmusic.subcount.Subcount;
import org.musicbox.common.vo.neteasecloudmusic.user.UserVo;
import org.musicbox.compatibility.neteasecloudmusic.CollectCompatibility;
import org.musicbox.compatibility.neteasecloudmusic.UserCompatibility;
import org.musicbox.controller.neteasecloudmusicapi.BaseController;
import org.musicbox.pojo.SysUserPojo;
import org.musicbox.pojo.TbCollectPojo;
import org.musicbox.pojo.TbCollectTagPojo;
import org.musicbox.pojo.TbTagPojo;
import org.musicbox.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private UserCompatibility user;
    
    @Autowired
    private CollectCompatibility collect;
    
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
    public NeteaseResult userPlayList(@RequestParam("uid") String uid, Long pageSize, Long pageIndex) {
        if (pageIndex == null || pageSize == null) {
            pageIndex = 0L;
            pageSize = 30L;
        }
        PlayListVo playListVo = new PlayListVo();
        playListVo.setPlaylist(new ArrayList<>());
        playListVo.setVersion("0");
        
        
        // 如果歌单查询没有值，直接返回
        Page<TbCollectPojo> collectPojoPage = user.getPlayList(uid, pageIndex, pageSize);
        // 是否有下一页
        playListVo.setMore(collectPojoPage.hasNext());
        List<TbCollectPojo> collectPojoList = collectPojoPage.getRecords();
        if (collectPojoList == null || collectPojoList.isEmpty()) {
            NeteaseResult neteaseResult = new NeteaseResult();
            neteaseResult.putAll(BeanUtil.beanToMap(playListVo));
            return neteaseResult.success();
        }
        // 导出歌单id
        List<Long> collectIds = collectPojoList.stream().map(TbCollectPojo::getId).collect(Collectors.toList());
        // 根据歌单和tag的中间表来获取tag id列表
        List<TbCollectTagPojo> collectIdAndTagsIdList = collect.getCollectTagIdList(collectIds);
        // 根据tag id 列表获取tag Name列表
        List<Long> tagIdList = collectIdAndTagsIdList.stream()
                                                     .map(TbCollectTagPojo::getTagId)
                                                     .collect(Collectors.toList());
        List<TbTagPojo> collectTagList = collect.getTagPojoList(tagIdList);
        
        
        for (TbCollectPojo tbCollectPojo : collectPojoList) {
            PlaylistItem item = new PlaylistItem();
            // 是否订阅
            item.setSubscribed(false);
            // 创作者
            Creator creator = new Creator();
            item.setCreator(creator);
            // 用户ID
            item.setUserId(tbCollectPojo.getUserId());
            // 封面图像ID
            item.setCoverImgUrl(tbCollectPojo.getPic());
            // 创建时间
            item.setCreateTime(tbCollectPojo.getCreateTime().getNano());
            // 描述
            item.setDescription(tbCollectPojo.getDescription());
            // 判断中间表是否有值
            // 判断tag表是否有值
            if (!collectIdAndTagsIdList.isEmpty() && collectTagList != null && !collectTagList.isEmpty()) {
                // 歌单tag
                // 先查找歌单和tag中间表，再查找tag记录表
                List<String> tags = collectIdAndTagsIdList.stream()
                                                          .filter(tbCollectTagPojo -> tbCollectTagPojo.getCollectId()
                                                                                                      .equals(tbCollectPojo.getId()))
                                                          .map(tbCollectTagPojo -> getTags(tbCollectTagPojo.getTagId(),
                                                                  collectTagList))
                                                          .collect(Collectors.toList());
                item.setTags(tags);
            }
            // 歌单名
            item.setName(tbCollectPojo.getPlayListName());
            // 歌单ID
            item.setId(tbCollectPojo.getId());
            
            playListVo.getPlaylist().add(item);
        }
        NeteaseResult neteaseResult = new NeteaseResult();
        neteaseResult.putAll(BeanUtil.beanToMap(playListVo));
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
    
    
    /**
     * 获取歌单tag
     *
     * @param tagId   tag id
     * @param tagList 风格
     * @return 风格名称
     */
    private String getTags(Long tagId, List<TbTagPojo> tagList) {
        for (TbTagPojo tag : tagList) {
            if (Objects.equals(tag.getId(), tagId)) {
                return tag.getTagName();
            }
        }
        return null;
    }
}
