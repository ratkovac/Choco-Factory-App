package beans;

import java.util.List;

public class Cart {
    private String id;
    private String userId;
    private List<CocoInCart> chocolates;
    private double totalPrice;
    
    public Cart(String id, List<CocoInCart> chocolates, double totalPrice, String userId) {
        this.id = id;
        this.chocolates = chocolates;
        this.totalPrice = totalPrice;
        this.userId = userId;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public List<CocoInCart> getChocolates() {
        return chocolates;
    }
    
    public void setChocolates(List<CocoInCart> chocolates) {
        this.chocolates = chocolates;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
