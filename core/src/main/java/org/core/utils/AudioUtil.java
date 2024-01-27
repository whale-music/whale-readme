package org.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

@Slf4j
public class AudioUtil {
    private AudioUtil() {
    }
    
    public static AudioFile getAudioInfo(File path) {
        AudioFile read = new AudioFile();
        try {
            read = AudioFileIO.read(path);
        } catch (InvalidAudioFrameException | CannotReadException | TagException | ReadOnlyFileException | IOException e) {
            log.info(e.getMessage(), e.getCause());
        }
        return read;
    }
    
    public static String getLevel(int rate) {
        // 标准
        if (128000 >= rate) {
            return "standard";
        }
        // 较高
        if (192000 == rate) {
            return "higher";
        }
        // 极高
        if (320_000 >= rate) {
            return "exhigh";
        }
        // 无损
        return "lossless";
        // hires rate < 1411_000
    }
}
