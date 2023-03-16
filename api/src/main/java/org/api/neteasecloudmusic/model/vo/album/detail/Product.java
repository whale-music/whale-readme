package org.api.neteasecloudmusic.model.vo.album.detail;

import lombok.Data;

import java.util.List;

@Data
public class Product{
	private int prefillBuyNum;
	private boolean hasMessage;
	private String prefillMsg;
	private long pubTime;
	private int canDraw;
	private String mvPlayUrl;
	private ExtInfo extInfo;
	private List<String> buynotes;
	private boolean isFree;
	private int price;
	private int albumType;
	private int saleDisplayType;
	private String mvTitle;
	private int albumfee;
	private boolean buysingle;
	private int bundling;
	private boolean finished;
	private int saleNum;
	private boolean showMv;
	private List<String> tags;
	private String donateMsg;
	private List<DescrItem> descr;
	private String mvCoverUrl;
	private boolean singleCanBoard;
	private int status;
}