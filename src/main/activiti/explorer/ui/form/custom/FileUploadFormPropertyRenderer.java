package org.activiti.explorer.ui.form.custom;

import java.io.IOException;
import java.io.OutputStream;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.custom.FileUploadFormType;
import org.activiti.engine.form.custom.UploadField2;
import org.activiti.engine.form.custom.util.UploadFileInputStream;
import org.activiti.engine.form.custom.util.UploadFileOutputStream;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;

import com.vaadin.ui.Field;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class FileUploadFormPropertyRenderer extends AbstractFormPropertyRenderer {

	private static final long serialVersionUID = -6575079959897466865L;

	FileUploadFormType formType = new FileUploadFormType();

	private UploadFileInputStream fieldValue;

	public FileUploadFormPropertyRenderer() {
		super(FileUploadFormType.class);
	}

	@Override
	public Field getPropertyField(FormProperty formProperty) {
		FileUploader fileUploader = new FileUploader();
		UploadField2 upload = new UploadField2(getPropertyLabel(formProperty), fileUploader);
		
		upload.setReceiver(fileUploader);
		upload.addListener((StartedListener) fileUploader);
		upload.addListener((SucceededListener) fileUploader);
		upload.addListener((FailedListener) fileUploader);

		upload.setEnabled(formProperty.isWritable());
		upload.setRequired(formProperty.isRequired());
		upload.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));

		return upload;
	}

	// Implement both receiver that saves upload in a file and
	// listener for successful upload
	class FileUploader implements Receiver, SucceededListener, StartedListener, FailedListener {
		private static final long serialVersionUID = -1276759102490466761L;

		// Create upload stream
		private UploadFileOutputStream fos = null; // Output stream to write to

		public OutputStream receiveUpload(String filename, String mimeType) {

			// Open the file for writing.
			fos = new UploadFileOutputStream();
			fos.setFilename(filename);
			fos.setMimeType(mimeType);

			return fos; // Return the output stream to write to
		}

		public void uploadSucceeded(SucceededEvent event) {

			if (fieldValue != null) {
				try {
					fieldValue.close();
				} catch (IOException e) {
				} finally {
					fieldValue = null;
				}
			}

			fieldValue = new UploadFileInputStream(fos.getFilename(), fos.getMimeType(), fos.toByteArray());
		}

		public void uploadStarted(StartedEvent event) {
			if (fieldValue != null) {
				try {
					fieldValue.close();
				} catch (IOException e) {
				}
			}
			fieldValue = null;

		}

		@Override
		public void uploadFailed(FailedEvent event) {
			if (fieldValue != null) {
				try {
					fieldValue.close();
				} catch (IOException e) {
				}
			}
			fieldValue = null;
		}
	};

	@Override
	public String getFieldValue(FormProperty formProperty, Field field) {
		return formType.convertModelValueToFormValue(fieldValue);
	}
}
