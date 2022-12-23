package org.api.test;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.api.model.LikePlay;
import org.api.model.url.SongUrl;
import org.api.utils.RequestMusic163;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class TestDownloadMusic {
    String dest = "D:\\music";
    
    @Test
    public void testDownloadMusic() {
        Log log = Log.get();
        
        String playId = "6389917304";
        String cookie = "MUSIC_U=afceb1d1edc22023fb24b900e2d84b1a3356f40b4c95cdc3332c107e08cf9238993166e004087dd3beb58a6aba726f0f3ce603cf8cd7f4cb6a1b92b9f0e5594e079514a26eb961c1a0d2166338885bd7;Cookie=d33658da9213990dece8c775a34a34c5c2fb33260a1fb19bada0beb8f6faacc5519e07624a9f005385583814e33238d94932d1745580b0f93726809422c6334bdebf8de6ed45b6347a561ba977ae766d; NMTID=00OHnqUmllyTs4zBEXZg4F6NoDnMkkAAAGFLvTgXQ; __csrf=b1656f1145c34b42866e27f19fc81f0c; __remember_me=true; MUSIC_U=d33658da9213990dece8c775a34a34c5c2fb33260a1fb19bada0beb8f6faacc5519e07624a9f005385583814e33238d94932d1745580b0f93726809422c6334bdebf8de6ed45b6347a561ba977ae766d";
        LikePlay like = RequestMusic163.like(playId, cookie);
        
        List<Integer> ids = like.getIds();
        int allPageIndex = PageUtil.totalPage(ids.size(), 20);
        
        File saveFile = FileUtil.mkdir(dest);
        List<String> fileNames = FileUtil.listFileNames(dest);
        List<String> fileMd5s = fileNames.stream().map(s -> StringUtils.split(s, '.')[0]).collect(Collectors.toList());
        
        for (int i = 0; i < allPageIndex; i++) {
            List<Integer> page = ListUtil.page(i, 20, ids);
            SongUrl songUrl = RequestMusic163.getSongUrl(page, cookie);
            songUrl.getData().parallelStream().forEach(datum -> {
                if (fileMd5s.contains(datum.getMd5())) {
                    log.info("已下载文件: {}", datum.getMd5());
                } else {
                    try {
                        // log.info("正在下载歌曲ID: {}, md5: {}", datum.getId(), datum.getMd5());
                        if (StringUtils.isBlank(datum.getUrl())) {
                            log.error("该歌曲没有地址id:{} md5: {}", datum.getUrl(), datum.getMd5());
                            return;
                        }
                        String[] split = datum.getUrl().split("/");
                        File name = new File(saveFile.getAbsolutePath(), split[split.length - 1]);
                        RequestMusic163.download(datum.getUrl(), name);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return;
                    }
                    log.info("下载成功: {} 比特率: {} 时常: {} 大小{}", datum.getMd5(), datum.getBr(), datum.getTime(), datum.getSize());
                }
            });
        }
    }
}
