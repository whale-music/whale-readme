package org.api.nmusic.model.vo.artist.mvs;

import lombok.Data;

@Data
public class ArtistMvRes {
    private int duration;
    private String imgurl;
    private int playCount;
    private String publishTime;
    private String imgurl16v9;
    private boolean subed;
    private Artist artist;
    private String name;
    private String artistName;
    private int id;
    private int status;
}