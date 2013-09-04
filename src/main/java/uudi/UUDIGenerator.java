package uudi;

public class UUDIGenerator {
	
	private String name;
	
	private String version;

	/**
	 * The name of the generator (Application, Library, etc) used to generate (create) the 
	 * JSON message.
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * An undefined version string used in identify the version of the JSON message generator.
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
