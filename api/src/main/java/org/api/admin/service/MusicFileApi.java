package org.api.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.api.admin.model.res.MusicFileRes;
import org.core.config.SaveConfig;
import org.core.pojo.TbMusicUrlPojo;
import org.core.service.TbMusicUrlService;
import org.oss.factory.OSSFactory;
import org.oss.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("MusicFileApi")
public class MusicFileApi {
    /**
     * 音乐地址服务
     */
    @Autowired
    private TbMusicUrlService musicUrlService;
    
    /**
     * 访问音乐文件地址
     */
    @Autowired
    private SaveConfig config;
    
    
    public List<MusicFileRes> getMusic(Set<String> musicIds) {
        List<MusicFileRes> files = new ArrayList<>();
        List<TbMusicUrlPojo> list = musicUrlService.list(Wrappers.<TbMusicUrlPojo>lambdaQuery().in(TbMusicUrlPojo::getMusicId, musicIds));
        for (TbMusicUrlPojo tbMusicUrlPojo : list) {
            MusicFileRes musicFileRes = new MusicFileRes();
            try {
                OSSService aList = OSSFactory.ossFactory("AList");
                String musicAddresses = aList.getMusicAddresses(
                        config.getHost(),
                        config.getObjectSave(),
                        tbMusicUrlPojo.getMd5() + "." + tbMusicUrlPojo.getEncodeType());
                musicFileRes.setId(String.valueOf(tbMusicUrlPojo.getMusicId()));
                musicFileRes.setSize(tbMusicUrlPojo.getSize());
                musicFileRes.setLevel(tbMusicUrlPojo.getLevel());
                musicFileRes.setMd5(tbMusicUrlPojo.getMd5());
                musicFileRes.setRawUrl(musicAddresses);
                musicFileRes.setExists(true);
            } catch (Exception e) {
                musicFileRes.setRawUrl("");
                musicFileRes.setExists(false);
                continue;
            }
            files.add(musicFileRes);
        }
        return files;
    }
}
