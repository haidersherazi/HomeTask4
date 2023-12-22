
public class Metadata {

	private String version;
	private String projectName;
	private String description;

	public Metadata (String pN, String v, String d) {
    
    	this.projectName = pN;
    	this.version = v;
    	this.description = d;
    
    }

    // Getters and setters
    
    public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
