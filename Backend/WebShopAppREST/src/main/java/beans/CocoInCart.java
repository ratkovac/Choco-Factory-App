package beans;

public class CocoInCart {
	private String id;
    private String idChocolate;
    private int quantity;
       
    
	public CocoInCart() {
		super();
	}

	public CocoInCart(String id, String idChocolate, int quantity) {
		super();
		this.id = id;
		this.idChocolate = idChocolate;
		this.quantity = quantity;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdChocolate() {
		return idChocolate;
	}
	public void setIdChocolate(String idChocolate) {
		this.idChocolate = idChocolate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    
}
