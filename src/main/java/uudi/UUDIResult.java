package uudi;

import java.util.Date;
import java.util.List;

public class UUDIResult {
	
	private List<UUDIUploadKey> uploadKeys;

	private UUDIGenerator generator;
	
	private Date currentTime;

	private List<UUDIRowSet> rowsets;

	/**
	 * An array of objects containing per endpoint upload keys
	 * @return
	 */
	public List<UUDIUploadKey> getUploadKeys() {
		return uploadKeys;
	}

	public void setUploadKeys(List<UUDIUploadKey> uploadKeys) {
		this.uploadKeys = uploadKeys;
	}

	/**
	 * An object with a “name” and “version” pairs to identify the JSON message generator.
	 * @return
	 */
	public UUDIGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(UUDIGenerator generator) {
		this.generator = generator;
	}

	/**
	 * The current data/time in ISO 8601 format (example 2011-10-22T15:46:00+00:00) when 
	 * the JSON message was created. It may also be changed by any forwarders as well 
	 * when the message is re-sent.
	 * @return
	 */
	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * An array of data objects as specified in Rowsets 
	 * {@link http://dev.eve-central.com/unifieduploader/start#rowsets}.
	 * @return
	 */
	public List<UUDIRowSet> getRowsets() {
		return rowsets;
	}
	
	public void setRowsets(List<UUDIRowSet> rowsets){
		this.rowsets = rowsets;
	}
 
}
