package org.api.admin.service;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.PageUserReq;
import org.api.admin.model.req.SaveOrUpdateUserReq;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.PageUserRes;
import org.api.admin.model.res.SaveOrUpdateUserRes;
import org.api.admin.model.res.UserRes;
import org.api.admin.utils.MyPageUtil;
import org.api.common.service.QukuAPI;
import org.common.exception.BaseException;
import org.common.result.ResultCode;
import org.common.utils.ExceptionUtil;
import org.core.common.constant.RoleConstant;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.utils.TokenUtil;
import org.core.utils.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service(AdminConfig.ADMIN + "UserApi")
public class UserApi {
    
    // 用户服务
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private QukuAPI qukuAPI;
    
    @Autowired
    private HttpRequestConfig requestConfig;
    
    @Autowired
    private TbUserCollectService userCollectService;
    
    @Autowired
    private TbUserAlbumService userAlbumService;
    
    @Autowired
    private TbUserArtistService userArtistService;
    
    @Autowired
    private TbUserMvService userMvService;
    
    @Autowired
    private TbCollectMusicService collectMusicService;
    
    public void createAccount(UserReq req) {
        accountService.createAccount(req);
    }
    
    public UserRes login(String phone, String password) {
        SysUserPojo userPojo = accountService.login(phone, password);
        String sign = TokenUtil.sign(userPojo.getUsername(), userPojo);
        UserRes userRes = new UserRes();
        BeanUtils.copyProperties(userPojo, userRes);
        userRes.setToken(sign);
        return userRes;
    }
    
    public UserConvert getUserInfo(Long id) {
        SysUserPojo byId = accountService.getById(id);
        if (Objects.isNull(byId)) {
            return new UserConvert();
        }
        UserConvert res = new UserConvert(byId);
        res.setBackgroundPicUrl(qukuAPI.getUserBackgroundPicUrl(byId.getId()));
        res.setAvatarUrl(qukuAPI.getUserAvatarPicUrl(byId.getId()));
        return res;
    }
    
    public Page<PageUserRes> getUserPage(PageUserReq user) {
        user.setPage(MyPageUtil.checkPage(user.getPage()));
        LambdaQueryWrapper<SysUserPojo> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(user.getUsername()), SysUserPojo::getUsername, user.getUsername());
        wrapper.like(StringUtils.isNotBlank(user.getNickname()), SysUserPojo::getNickname, user.getNickname());
        
        Page<SysUserPojo> page = accountService.page(new Page<>(user.getPage().getPageIndex(), user.getPage().getPageNum()), wrapper);
        List<SysUserPojo> records = page.getRecords();
        Page<PageUserRes> resPage = new Page<>();
        BeanUtils.copyProperties(page, resPage);
        List<PageUserRes> collect = records.parallelStream().map(PageUserRes::new).toList();
        resPage.setRecords(collect);
        return resPage;
    }
    
    public SaveOrUpdateUserRes saveOrUpdateUser(SaveOrUpdateUserReq saveOrUpdateUserReq) {
        // 防止创建普通用户创建管理员
        if (UserUtil.getUser().getIsAdmin()) {
            // 不允许修改管理员用户状态
            ExceptionUtil.isNull(saveOrUpdateUserReq.getIsAdmin() && Boolean.FALSE.equals(saveOrUpdateUserReq.getStatus()),
                    ResultCode.ADMIN_USER_NOT_EDIT_STATUS);
            SysUserPojo user = accountService.getUserByName(saveOrUpdateUserReq.getUsername());
            if (Objects.isNull(saveOrUpdateUserReq.getId())) {
                if (Objects.nonNull(user)) {
                    throw new BaseException(ResultCode.USER_HAS_EXISTED);
                }
                accountService.saveOrUpdate(saveOrUpdateUserReq);
            } else {
                // 校验是否有用户ID
                boolean userFlag = Objects.isNull(user);
                ExceptionUtil.isNull(userFlag, ResultCode.USER_NOT_EXIST);
                accountService.updateById(saveOrUpdateUserReq);
            }
            // 更新用户头像
            if (StringUtils.isNotBlank(saveOrUpdateUserReq.getAvatarTempFile())) {
                File file = new File(requestConfig.getTempPath(), saveOrUpdateUserReq.getAvatarTempFile());
                ExceptionUtil.isNull(FileUtil.isEmpty(file), ResultCode.FILENAME_NO_EXIST);
                qukuAPI.saveOrUpdateAvatarPicFile(saveOrUpdateUserReq.getId(), file);
            }
            // 更新用户背景
            if (StringUtils.isNotBlank(saveOrUpdateUserReq.getBackgroundTempFile())) {
                File file = new File(requestConfig.getTempPath(), saveOrUpdateUserReq.getBackgroundTempFile());
                ExceptionUtil.isNull(FileUtil.isEmpty(file), ResultCode.FILENAME_NO_EXIST);
                qukuAPI.saveOrUpdateBackgroundPicFile(saveOrUpdateUserReq.getId(), file);
            }
            SaveOrUpdateUserRes res = new SaveOrUpdateUserRes();
            BeanUtils.copyProperties(saveOrUpdateUserReq, res);
            return res;
        } else {
            throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
        }
    }
    
    public Set<String> getExampleRoles() {
        return RoleConstant.getExampleRole();
    }
    
    public void deleteUser(Long id) {
        SysUserPojo byId = accountService.getById(id);
        if (byId.getIsAdmin()) {
            throw new BaseException(ResultCode.ADMIN_CANNOT_DELETE);
        }
        // 删除用户专辑
        userAlbumService.remove(Wrappers.<TbUserAlbumPojo>lambdaQuery().eq(TbUserAlbumPojo::getUserId, id));
        // 删除用户关注歌手
        userArtistService.remove(Wrappers.<TbUserArtistPojo>lambdaQuery().eq(TbUserArtistPojo::getUserId, id));
        LambdaQueryWrapper<TbUserCollectPojo> userCollect = Wrappers.<TbUserCollectPojo>lambdaQuery().eq(TbUserCollectPojo::getUserId, id);
        // 删除用户创建歌单
        List<TbUserCollectPojo> userCollectPojoList = userCollectService.list(userCollect);
        List<Long> collectIds = userCollectPojoList.parallelStream().map(TbUserCollectPojo::getCollectId).toList();
        collectMusicService.remove(Wrappers.<TbCollectMusicPojo>lambdaQuery().eq(TbCollectMusicPojo::getCollectId, collectIds));
        userCollectService.remove(userCollect);
        // 删除用户MV
        userMvService.remove(Wrappers.<TbUserMvPojo>lambdaQuery().eq(TbUserMvPojo::getUserId, id));
        // 删除用户
        accountService.removeById(id);
    }
    
    public void updateUserPassword(Long id, String username, String nickname, String password) {
        ExceptionUtil.isNull(!Objects.equals(UserUtil.getUser().getId(), id), ResultCode.PERMISSION_NO_ACCESS);
        SysUserPojo byId = accountService.getById(id);
        byId.setUsername(username);
        byId.setNickname(nickname);
        byId.setPassword(password);
        byId.updateById();
    }
}
