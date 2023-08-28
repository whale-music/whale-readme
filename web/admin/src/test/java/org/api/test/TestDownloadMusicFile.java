package org.api.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.utils.RequestMusic163;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
class TestDownloadMusicFile {
    String dest = "D:\\temp\\music";
    
    @Test
    void testDownloadMusic() {
        String playId = "6389917304";
        String cookie = "MUSIC_U=d33658da9213990dece8c775a34a34c53c3cc0a4f13bf25a70879d8c69860294519e07624a9f0053ed289331763956b057e5a898e3c2eb093726809422c6334bdebf8de6ed45b634d4dbf082a8813684";
        List<Long> likeIds = RequestMusic163.like(playId, cookie);
        assert CollUtil.isNotEmpty(likeIds);
        int allPageIndex = PageUtil.totalPage(likeIds.size(), 20);
        
        File saveFile = FileUtil.mkdir(dest);
        // 获取所有文件名
        List<String> fileNames = FileUtil.listFileNames(dest);
        List<String> fileMd5s = fileNames.stream().map(s -> StringUtils.split(s, '.')[0]).collect(Collectors.toList());
        Map<String, String> collect = fileNames.stream().collect(Collectors.toMap(s -> StringUtils.split(s, '.')[0], String::new));
        
        for (int i = 0; i < allPageIndex; i++) {
            List<Long> page = ListUtil.page(i, 20, likeIds);
            List<Map<String, Object>> songUrl = RequestMusic163.getSongUrl(page, cookie, 1);
            songUrl.parallelStream().forEach(datum -> {
                String md5Value = MapUtil.get(datum, "md5", String.class, "");
                if (CollUtil.contains(fileMd5s, md5Value)) {
                    log.info("\033[0;33m已下载文件: {}\033[0m", md5Value);
                } else {
                    try {
                        // log.info("正在下载歌曲ID: {}, md5: {}", datum.getId(), datum.getMd5());
                        String urlValue = MapUtil.get(datum, "url", String.class);
                        if (StringUtils.isBlank(urlValue)) {
                            log.error("\033[0;31m该歌曲没有地址id:{} md5: {}\033[0m", MapUtil.get(datum, "id", String.class), md5Value);
                            return;
                        }
                        // MD5作为文件名
                        File name = new File(saveFile.getAbsolutePath(), md5Value + "." + MapUtil.get(datum, "type", String.class));
                        RequestMusic163.download(urlValue, name);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return;
                    }
                    log.info("\033[0;32m下载成功: {} 比特率: {} 时常: {} 大小{}\033[0m",
                            md5Value,
                            MapUtil.get(datum, "br", String.class),
                            MapUtil.get(datum, "time", String.class),
                            MapUtil.get(datum, "size", String.class));
                }
                // 校验下载文件md5
                if (collect.get(md5Value) != null) {
                    // 检测所有下载文件MD5值是否相同，不相同则删除
                    File dataFile = new File(dest + FileUtil.FILE_SEPARATOR + collect.get(md5Value));
                    String md5 = SecureUtil.md5(dataFile);
                    if (StringUtils.equals(md5Value, md5)) {
                        log.info("Md5值一样");
                        collect.remove(md5);
                    } else {
                        log.error("\033[0;31mMd5值不一样!!!!!!!\033[0m\n{}\t{}", md5Value, md5);
                        FileUtil.del(dataFile);
                        log.error("\n\033[0;31m已删除不一样数据，请重新运行\033[0m");
                    }
                }
                
            });
        }
        // 输出没有对应md5值，不确定是错误文件还是下载列表中没有该音乐文件
        for (String s : collect.keySet()) {
            System.out.println("no remove file list " + collect.get(s));
        }
    }
}
