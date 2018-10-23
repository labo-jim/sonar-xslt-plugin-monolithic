package labo.jim.schematron;

public class PendingRule {
	
	private String id;
	private String name;
	private String description;
	
	// TODO severity, catype, etc...

	
	public String getKey() {
		return id;
	}
	public void setKey(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "SchematronAssertReport [id=" + id + ", name=" + name + "]";
	}
	
	
	
	

}
