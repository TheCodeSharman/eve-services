package uudi;

public class UUDIUploadKey {
	
	private String name;
	
	private String key;

	/**
	 * The name (initials) of the endpoint the “key” is for.
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The “key” used by endpoint to ID the 'source' the data is from. This can be a simple ID 
	 * number, or some kind of message digest. See the Key Types 
	 * {@link http://dev.eve-central.com/unifieduploader/start#key_types}
	 * section for more in depth information.
	 * @return
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
