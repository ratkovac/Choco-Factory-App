package beans;

public class Coco {
	
    private String id;
    private String name;
    private double cost;
    private String category;
    private String type;
    private double mass;
    private String details;
    private String picture;
    private int stock;
    private boolean isDeleted;
    private String factoryId;
    private String status;


	
	public Coco() {
		this.stock = 0;
	}

	public Coco(String id, String name, double cost, String category, String type, double mass,
			String details, String picture, int stock, boolean isDeleted, String factoryId, String status) {
		super();
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.category = category;
		this.type = type;
		this.mass = mass;
		this.details = details;
		this.picture = picture;
		this.stock = stock;
		this.isDeleted = isDeleted;
		this.factoryId = factoryId;
		this.status = status;
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

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	@Override
	public String toString() {
		return "Coco [id=" + id + ", name=" + name + ", cost=" + cost + ", category=" + category + ", type=" + type
				+ ", mass=" + mass + ", details=" + details + ", picture=" + picture + ", stock=" + stock
				+ ", isDeleted=" + isDeleted + ", factoryId=" + factoryId + ", status=" + status + "]";
	}
	
	
	
}