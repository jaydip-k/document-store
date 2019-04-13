package com.util;

import java.util.Date;

public class ObjectMetaData {
	private Date lastModifiedAt;
	private Date createddAt;
	private Date lastAccessedAt;
	private String size;

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public Date getCreateddAt() {
		return createddAt;
	}

	public void setCreateddAt(Date createddAt) {
		this.createddAt = createddAt;
	}

	public Date getLastAccessedAt() {
		return lastAccessedAt;
	}

	public void setLastAccessedAt(Date lastAccessedAt) {
		this.lastAccessedAt = lastAccessedAt;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
