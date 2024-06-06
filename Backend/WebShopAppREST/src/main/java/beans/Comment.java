package beans;

public class Comment {
	private String id;
	private String text;
	private String factoryId;
	
	public Comment() {
		
	}
	
	public Comment(String id, String text, String factoryId) {
		super();
		this.id = id;
		this.text = text;
		this.setFactoryId(factoryId);
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