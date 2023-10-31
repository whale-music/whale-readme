package org.core.oss.service.impl.alist.model.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataItem {
	private Integer size;
	private Object related;
	private String thumb;
	private String provider;
	private Boolean isDir;
	private String name;
	private String sign;
	private String modified;
	private String readme;
	private Integer type;
	private String raw_url;
}
