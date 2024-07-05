package beans;

public class Comment {
	private String id;
	private String text;
	private String factoryId;
	private boolean isValid;
	
	public Comment() {
		super();
	}
	
	public Comment(String id, String text, String factoryId, boolean isValid) {
		super();
		this.id = id;
		this.text = text;
		this.setFactoryId(factoryId);
		this.isValid = isValid;
	}
	
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}
		
	
}