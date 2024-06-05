package beans;

public class Coco {
	
    private String id;
    private String name;
    private double cost;
    private String category;
    private String type;
    private String status;
    private double mass;
    private String details;
    private String picture;
    private int stock;
    private boolean isDeleted;

	
	public Coco() {
		this.stock = 0;
	}

	public Coco(String id, String name, double cost, String category, String type, String status, double mass,
			String details, String picture, int stock, boolean isDeleted) {
		super();
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.category = category;
		this.type = type;
		this.status = status;
		this.mass = mass;
		this.details = details;
		this.picture = picture;
		this.stock = stock;
		this.isDeleted = isDeleted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
}