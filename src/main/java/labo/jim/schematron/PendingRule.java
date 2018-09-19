package labo.jim.schematron;

public class PendingRule {
	
	private String id;
	private String textContent;
	
	// TODO severity, catype, etc...

	
	public String getKey() {
		return id;
	}
	public void setKey(String id) {
		this.id = id;
	}
	public String getName() {
		return textContent;
	}
	public void setName(String textContent) {
		this.textContent = textContent;
	}
	@Override
	public String toString() {
		return "SchematronAssertReport [id=" + id + ", textContent=" + textContent + "]";
	}
	
	
	

}
