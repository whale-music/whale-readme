package org.api.nmusic.model.vo.user.record;

import lombok.Data;

@Data
public class UserRecordRes{
	private Song song;
	private int playCount;
	private int score;
}