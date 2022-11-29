package org.core.common.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioInfoVo {
    // 音乐名
    String musicName;
    // 音乐别名
    String aliaName;
    // 音乐类型
    String type;
    // 歌手
    List<String> singer;
    // 专辑
    String album;
    // 音乐歌词
    String lyric;
    // 音乐时长
    Integer timeLength;
    // 音乐质量
    String quality;
    // 大小
    Long size;
}
