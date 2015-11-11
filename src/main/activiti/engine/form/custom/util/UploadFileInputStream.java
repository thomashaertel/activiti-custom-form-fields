package org.activiti.engine.form.custom.util;

import java.io.ByteArrayInputStream;

public class UploadFileInputStream extends ByteArrayInputStream {

	private String mimeType;
	private String filename;
	
	public UploadFileInputStream(String filename, String mimeType, byte[] buf) {
		super(buf);
		
		this.filename = filename;
		this.mimeType = mimeType;
	}

	public UploadFileInputStream(String filename, String mimeType, byte[] buf, int offset, int length) {
		super(buf, offset, length);
		
		this.filename = filename;
		this.mimeType = mimeType;
	}

	public String getFilename() {
		return filename;
	}

	public String getMimeType() {
		return mimeType;
	}
}
