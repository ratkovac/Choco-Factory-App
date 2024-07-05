package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Cart;
import beans.Coco;
import beans.CocoInCart;
import beans.Purchase;

public class CartDAO {

    private HashMap<String, Cart> carts = new HashMap<>();
    private String fileLocation;
    private CocoInCartDAO cocoInCartDAO;
    private PurchaseDAO purchaseDAO;
    private CocoDAO cocoDAO;
    private FactoryDAO factoryDAO;
    private UserDAO userDAO;
    
    public CartDAO() {
        // Initialize with some dummy data for testing
    }

    public CartDAO(String contextPath) {
        this.fileLocation = "C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\carts.csv";
        System.out.println(fileLocation);
        cocoInCartDAO = new CocoInCartDAO(contextPath);
        purchaseDAO = new PurchaseDAO(contextPath);
        cocoDAO = new CocoDAO(contextPath);
        factoryDAO = new FactoryDAO(contextPath);
        userDAO = new UserDAO(contextPath);
        loadCarts(fileLocation);
    }

    public List<Cart> findAll() {
        return new ArrayList<>(carts.values());
    }

    public Cart findCart(String id) {
        return carts.get(id);
    }

    public Cart updateCart(String id, Cart cart) {
        Cart existingCart = carts.containsKey(id) ? carts.get(id) : null;
        if (existingCart == null) {
            return saveCart(cart);
        } else {
            existingCart.setId(cart.getId());
            existingCart.setChocolates(cart.getChocolates());
            existingCart.setTotalPrice(cart.getTotalPrice());
            existingCart.setUserId(cart.getUserId());
        }

        saveCartsToFile();
        return existingCart;
    }

    public Cart saveCart(Cart cart) {
        cart.setId(generateNewId());
        carts.put(cart.getId(), cart);
        ArrayList<CocoInCart> chocolates = new ArrayList<CocoInCart>();
        ArrayList<Coco> cocos = new ArrayList<Coco>();
        System.out.println("VELICINA: " + cart.getChocolates().size());
        for(CocoInCart coco : cart.getChocolates()) {
        	chocolates.add(cocoInCartDAO.saveCocoInCart(coco));
        	cocos.add(cocoDAO.findCoco(coco.getIdChocolate()));
        }
        cart.setChocolates(chocolates);
        saveCartsToFile();
        Coco firstCoco = cocos.get(0);
        String factoryId = firstCoco.getFactoryId();
        Purchase purchase = new Purchase();
        purchase.setPrice(cart.getTotalPrice());
        purchase.setStatus("Obrada");
        purchase.setChocolates(cocos);
        purchase.setChocolates(cocos);
        purchase.setFactory(factoryDAO.findFactory(factoryId));
        System.out.println("OVO JE Factory:" + purchase.getFactory().getId());
        System.out.println("OVO JE USER ID:" + cart.getUserId());
        purchase.setUser(userDAO.getUserById(cart.getUserId()));
        System.out.println("OVO JE USER:" + purchase.getUser().getId());
        System.out.println("************************************************************************");
        purchaseDAO.savePurchase(purchase);
        return cart;
    }

    public boolean deleteCart(String id) {
        if (carts.containsKey(id)) {
            carts.remove(id);
            saveCartsToFile();
            return true; // Successfully deleted
        } else {
            return false; // Object with given ID not found
        }
    }

    private String generateNewId() {
        int newId = carts.keySet().stream()
                .mapToInt(id -> Integer.parseInt(id))
                .max()
                .orElse(0) + 1;
        return String.valueOf(newId);
    }

    private void loadCarts(String filePath) {
        BufferedReader in = null;
        carts.clear();
        try {
            File file = new File(filePath);
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.startsWith("#"))
                    continue;
                StringTokenizer st = new StringTokenizer(line, ";");
                String id = st.nextToken().trim();
                String userId = st.nextToken().trim();
                String totalPrice = st.nextToken().trim();

                List<CocoInCart> chocolates = new ArrayList<>();
                while (st.hasMoreTokens()) {
                    String chocolateId = st.nextToken().trim();
			        String[] chocolateIds = chocolateId.split(",");
			        for (String cocoId : chocolateIds) {
	                    chocolates.add(cocoInCartDAO.findCocoInCart(cocoId)); // Assuming default quantity is 1
			        }
                }
                carts.put(id, new Cart(id, chocolates, Double.parseDouble(totalPrice), userId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveCartsToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation))) {
            for (Cart cart : carts.values()) {
                out.write(cart.getId() + ";" + cart.getUserId() + ";" + cart.getTotalPrice() + ";");

                // Prolazak kroz čokolade
                List<CocoInCart> chocolates = cart.getChocolates();
                for (int i = 0; i < chocolates.size(); i++) {
                    CocoInCart chocolate = chocolates.get(i);
                    out.write(chocolate.getId());
                    if (i < chocolates.size() - 1) {
                        out.write(","); // Dodaj zarez osim nakon poslednjeg elementa
                    }
                }
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
