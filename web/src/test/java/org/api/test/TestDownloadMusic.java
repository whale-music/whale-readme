package org.api.test;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.api.model.LikePlay;
import org.api.model.url.SongUrl;
import org.api.utils.RequestMusic163;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class TestDownloadMusic {
    String dest = "D:\\temp\\music";
    
    @Test
    void testDownloadMusic() {
        Log log = Log.get();
    
        String playId = "6389917304";
        String cookie = "MUSIC_U=d33658da9213990dece8c775a34a34c50b50b01f699cc9e22a9ed7c459f23d1f519e07624a9f0053af3a6f5228cb1247688c59aaba91d09f3726809422c6334bdebf8de6ed45b634d4dbf082a8813684";
        LikePlay like = RequestMusic163.like(playId, cookie);
    
        List<Long> ids = like.getIds();
        int allPageIndex = PageUtil.totalPage(ids.size(), 20);
    
        File saveFile = FileUtil.mkdir(dest);
        List<String> fileNames = FileUtil.listFileNames(dest);
        List<String> fileMd5s = fileNames.stream().map(s -> StringUtils.split(s, '.')[0]).collect(Collectors.toList());
        // 获取所有文件名
        Map<String, String> collect = fileNames.stream().collect(Collectors.toMap(s -> StringUtils.split(s, '.')[0], String::new));
    
        for (int i = 0; i < allPageIndex; i++) {
            List<Long> page = ListUtil.page(i, 20, ids);
            SongUrl songUrl = RequestMusic163.getSongUrl(page, cookie, 1);
            songUrl.getData().parallelStream().forEach(datum -> {
                if (fileMd5s.contains(datum.getMd5())) {
                    log.info("\033[0;33m已下载文件: {}\033[0m", datum.getMd5());
                } else {
                    try {
                        // log.info("正在下载歌曲ID: {}, md5: {}", datum.getId(), datum.getMd5());
                        if (StringUtils.isBlank(datum.getUrl())) {
                            log.error("\033[0;31m该歌曲没有地址id:{} md5: {}\033[0m", datum.getUrl(), datum.getMd5());
                            return;
                        }
                        String[] split = datum.getUrl().split("/");
                        File name = new File(saveFile.getAbsolutePath(), split[split.length - 1]);
                        RequestMusic163.download(datum.getUrl(), name);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return;
                    }
                    log.info("\033[0;32m下载成功: {} 比特率: {} 时常: {} 大小{}\033[0m", datum.getMd5(), datum.getBr(), datum.getTime(), datum.getSize());
                }
                // 检测所有下载文件MD5值是否相同，不相同则删除
                File dataFile = new File(dest + "\\" + collect.get(datum.getMd5()));
                if (collect.get(datum.getMd5()) != null) {
                    String md5 = SecureUtil.md5(dataFile);
                    if (StringUtils.equals(datum.getMd5(), md5)) {
                        log.info("Md5值一样");
                        collect.remove(md5);
                    } else {
                        log.error("\033[0;31mMd5值不一样!!!!!!!\033[0m" + datum.getMd5() + "\n" + md5);
                        FileUtil.del(dataFile);
                        log.error("\n\033[0;31m已删除不一样数据\033[0m");
                    }
                    System.out.println(md5);
                }
    
            });
        }
        for (String s : collect.keySet()) {
            System.out.println("collect.get(s) = " + collect.get(s));
        }
    }
}
