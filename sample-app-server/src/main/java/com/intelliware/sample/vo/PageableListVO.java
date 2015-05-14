package com.intelliware.sample.vo;

import java.util.List;

import com.intelliware.sample.vo.MetadataVO;

public class PageableListVO<T> {
	
	private List<T> elements;
	private MetadataVO _metadata;

	public PageableListVO(List<T> resultList) {
		this.elements = resultList;
		this._metadata = new MetadataVO(Long.valueOf(resultList.size()));
	}

	public PageableListVO(List<T> resultList, long totalElements) {
		this.elements = resultList;
		this._metadata = new MetadataVO(totalElements);
	}

	public List<T> getElements() {
		return elements;
	}

	public void setElements(List<T> elements) {
		this.elements = elements;
	}

	public MetadataVO get_metadata() {
		return _metadata;
	}

	public void set_metadata(MetadataVO _metadata) {
		this._metadata = _metadata;
	}
}
