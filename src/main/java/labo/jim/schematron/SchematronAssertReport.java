package labo.jim.schematron;

public class SchematronAssertReport {
	
	private String id;
	private String textContent;
	
	// TODO severity, catype, etc...

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	@Override
	public String toString() {
		return "SchematronAssertReport [id=" + id + ", textContent=" + textContent + "]";
	}
	
	
	

}
