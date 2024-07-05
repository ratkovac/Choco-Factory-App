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

import beans.CocoInCart;
import beans.Factory;

public class CocoInCartDAO {

    private HashMap<String, CocoInCart> cocoInCartItems = new HashMap<>();
    private String fileLocation;

    public CocoInCartDAO(String contextPath) {
        //this.fileLocation = "C:\\Users\\janic\\FAX\\SEMESTAR 6\\Veb programiranje\\CocoFactory\\veb-projekat\\Backend\\WebShopAppREST\\src\\main\\webapp\\cocoInCarts.csv";
        this.fileLocation = "C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\cocoInCarts.csv";
        loadCocoInCartItems(fileLocation);
        System.out.println(cocoInCartItems.size());
    }

    public List<CocoInCart> findAll() {
        return new ArrayList<>(cocoInCartItems.values());
    }

    public CocoInCart findCocoInCart(String id) {
        return cocoInCartItems.get(id);
    }
    
    public CocoInCart updateCocoInCart(String id, CocoInCart cocoInCart) {
        CocoInCart existingItem = cocoInCartItems.containsKey(id) ? cocoInCartItems.get(id) : null;
        if (existingItem == null) {
            return saveCocoInCart(cocoInCart);
        } else {
            existingItem.setId(cocoInCart.getId());
            existingItem.setIdChocolate(cocoInCart.getIdChocolate());
            existingItem.setQuantity(cocoInCart.getQuantity());
        }

        saveCocoInCartItemsToFile();
        return existingItem;
    }

    public CocoInCart saveCocoInCart(CocoInCart cocoInCart) {
        cocoInCart.setId(generateNewId());
        cocoInCartItems.put(cocoInCart.getId(), cocoInCart);
        saveCocoInCartItemsToFile();
        return cocoInCart;
    }

    public boolean deleteCocoInCart(String id) {
        if (cocoInCartItems.containsKey(id)) {
            cocoInCartItems.remove(id);
            saveCocoInCartItemsToFile();
            return true; // Successfully deleted
        } else {
            return false; // Object with given ID not found
        }
    }

    private String generateNewId() {
        int newId = cocoInCartItems.keySet().stream()
                .mapToInt(id -> Integer.parseInt(id))
                .max()
                .orElse(0) + 1;
        return String.valueOf(newId);
    }

    private void loadCocoInCartItems(String filePath) {
        BufferedReader in = null;
        cocoInCartItems.clear();
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
                String chocolateId = st.nextToken().trim();
                int quantity = Integer.parseInt(st.nextToken().trim());

                cocoInCartItems.put(id, new CocoInCart(id, chocolateId, quantity));
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

    private void saveCocoInCartItemsToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation))) {
            for (CocoInCart item : cocoInCartItems.values()) {
                out.write(item.getId() + ";" + item.getIdChocolate() + ";" + item.getQuantity());
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
