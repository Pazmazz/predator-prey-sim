package classes.entity;

public class DebugInfo {

	private String primaryColor;
	private String secondaryColor;

	public DebugInfo() {
		//
		// Editable
		//
		this.primaryColor = "green";
		this.secondaryColor = "cyan";
	}

	public String getPrimaryColor() {
		return this.primaryColor;
	}

	public String getSecondaryColor() {
		return this.secondaryColor;
	}

	public DebugInfo setPrimaryColor(String color) {
		this.primaryColor = color;
		return this;
	}

	public DebugInfo setSecondaryColor(String color) {
		this.secondaryColor = color;
		return this;
	}
}