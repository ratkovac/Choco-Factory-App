package beans;

import java.util.List;

public class Purchase {
    private String id; // Jedinstveni identifikator (10 karaktera)
    private List<Coco> chocolates; // Čokolade koje su kupljene
    private Factory factory; // Fabrika iz koje je kupljeno
    private String purchaseDateTime; // Datum i vreme kupovine
    private double price; // Cena
    private User buyer; // Kupac (ime i prezime)
    private String status; // Status (Obrada, Odobreno, Odbijeno, Otkazano)
    
    
    public Purchase() {
		super();
	}

	// Constructor
    public Purchase(String id, List<Coco> chocolates, Factory factory, String purchaseDateTime,
                    double price, User buyerName, String status) {
        this.id = id;
        this.chocolates = chocolates;
        this.factory = factory;
        this.purchaseDateTime = purchaseDateTime;
        this.price = price;
        this.buyer = buyerName;
        this.status = status;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Coco> getChocolates() {
        return chocolates;
    }

    public void setChocolates(List<Coco> chocolates) {
        this.chocolates = chocolates;
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    public String getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(String purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return buyer;
    }

    public void setUser(User buyerName) {
        this.buyer = buyerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
