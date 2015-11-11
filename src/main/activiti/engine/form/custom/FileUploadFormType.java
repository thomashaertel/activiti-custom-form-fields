package org.activiti.engine.form.custom;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.form.custom.util.UploadFileInputStream;
import org.activiti.engine.impl.form.FormTypes;
import org.apache.commons.codec.binary.Base64;

import com.google.common.io.ByteStreams;

public class FileUploadFormType extends AbstractFormType {

	private static final long serialVersionUID = -5434864702798165136L;

	public static final String TYPE_NAME = "file";

	public static final String KEY_FILENAME = "filename";
	public static final String KEY_MIMETYPE = "mimeType";
	public static final String KEY_FILECONTENT_B64 = "fileContent";

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		final Properties properties = new Properties();
		try {
			properties.load(new StringReader(propertyValue));

			final byte[] content = Base64.decodeBase64(properties.getProperty(KEY_FILECONTENT_B64));

			return new UploadFileInputStream(properties.getProperty(KEY_FILENAME), properties.getProperty(KEY_MIMETYPE),
					content);
		} catch (IOException e) {
			// TODO Logging
		}

		return null;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		if (modelValue != null) {
			UploadFileInputStream stream = (UploadFileInputStream) modelValue;

			try {
				final String content = Base64.encodeBase64String(ByteStreams.toByteArray(stream));

				final Properties properties = new Properties();

				properties.setProperty(KEY_FILENAME, stream.getFilename());
				properties.setProperty(KEY_MIMETYPE, stream.getMimeType());
				properties.setProperty(KEY_FILECONTENT_B64, content);

				return properties.toString();

			} catch (IOException e) {
				// TODO Logging
			}
		}
		return null;
	}

}
