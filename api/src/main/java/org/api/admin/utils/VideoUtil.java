package org.api.admin.utils;

import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;

import java.io.File;
import java.io.IOException;

@Slf4j
public class VideoUtil {
    private VideoUtil() {
    }
    
    public static long getVideoDuration(File videoFile) throws IOException {
        if (videoFile.exists()) {
            try (FileChannelWrapper ch = NIOUtils.readableChannel(videoFile);) {
                MP4Demuxer demurer = MP4Demuxer.createMP4Demuxer(ch);
                DemuxerTrack videoTrack = demurer.getVideoTrack();
                double totalDuration = videoTrack.getMeta().getTotalDuration();
                log.info("video_duration: " + totalDuration);
                return (long) Math.ceil(totalDuration);
            }
        }
        throw new BaseException(ResultCode.FILENAME_NO_EXIST);
    }
}
