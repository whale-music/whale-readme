package org.api.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.api.admin.model.vo.MusicFileVo;
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
    
    
    public List<MusicFileVo> getMusic(Set<String> musicIds) {
        List<MusicFileVo> files = new ArrayList<>();
        List<TbMusicUrlPojo> list = musicUrlService.list(Wrappers.<TbMusicUrlPojo>lambdaQuery().in(TbMusicUrlPojo::getMusicId, musicIds));
        for (TbMusicUrlPojo tbMusicUrlPojo : list) {
            MusicFileVo musicFileVo = new MusicFileVo();
            try {
                OSSService aList = OSSFactory.ossFactory("AList");
                String musicAddresses = aList.getMusicAddresses(
                        config.getHost(),
                        config.getObjectSave(),
                        tbMusicUrlPojo.getMd5() + "." + tbMusicUrlPojo.getEncodeType());
                musicFileVo.setId(String.valueOf(tbMusicUrlPojo.getMusicId()));
                musicFileVo.setSize(tbMusicUrlPojo.getSize());
                musicFileVo.setLevel(tbMusicUrlPojo.getLevel());
                musicFileVo.setMd5(tbMusicUrlPojo.getMd5());
                musicFileVo.setRawUrl(musicAddresses);
                musicFileVo.setExists(true);
            } catch (Exception e) {
                musicFileVo.setRawUrl("");
                musicFileVo.setExists(false);
                continue;
            }
            files.add(musicFileVo);
        }
        return files;
    }
}
