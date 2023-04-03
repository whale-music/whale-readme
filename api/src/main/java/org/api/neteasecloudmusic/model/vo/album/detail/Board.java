package org.api.neteasecloudmusic.model.vo.album.detail;

import lombok.Data;

import java.util.List;

@Data
public class Board {
    private boolean hasTodayBoard;
    private boolean hasFansBoard;
    private List<Object> todayBoardPicIds;
    private String todayBoardDesc;
    private List<Object> todayBoardPics;
    private String totalBoardDesc;
    private List<Object> totalBoardPicIds;
    private List<Object> totalBoardPics;
}