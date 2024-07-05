package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Coco;
import beans.Factory;
import beans.Purchase;
import beans.User;
import enums.UserRole;

public class PurchaseDAO {

    private HashMap<String, Purchase> purchases = new HashMap<>();
    private String fileLocation;
    private CocoInCartDAO cocoInCartDAO; // DAO za čokolade u korpi
    private CocoDAO cocoDAO;
    private UserDAO userDAO; // DAO za korisnike (kupce)
    private FactoryDAO factoryDAO;

    public PurchaseDAO(String contextPath) {
        this.fileLocation = "C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\purchases.csv"; // Podesite putanju do CSV fajla
        cocoInCartDAO = new CocoInCartDAO(contextPath); // Inicijalizacija DAO za čokolade u korpi
        userDAO = new UserDAO(contextPath); // Inicijalizacija DAO za korisnike (kupce)
        cocoDAO = new CocoDAO(contextPath);
        factoryDAO = new FactoryDAO(contextPath);
        loadPurchases(fileLocation); // Učitavanje postojećih kupovina prilikom inicijalizacije
    }

    public List<Purchase> findAll() {
        return new ArrayList<>(purchases.values());
    }
    
    public List<Purchase> findAllByUser(String id) {
        List<Purchase> userPurchases = new ArrayList<>();     
        for (Purchase purchase : purchases.values()) {
            if (purchase.getUser().getId().equals(id)) {
                userPurchases.add(purchase); // Dodaj kupovinu u listu korisnikovih kupovina
            }
        }        
        return userPurchases; // Vrati listu kupovina koje pripadaju korisniku sa datim id
    }

    public Purchase findPurchase(String id) {
        return purchases.get(id);
    }

    public Purchase updatePurchase(String id, Purchase purchase) {
        Purchase existingPurchase = purchases.containsKey(id) ? purchases.get(id) : null;
        if (existingPurchase == null) {
            return savePurchase(purchase);
        } else {
            existingPurchase.setId(purchase.getId());
            existingPurchase.setChocolates(purchase.getChocolates());
            existingPurchase.setFactory(purchase.getFactory());
            existingPurchase.setPurchaseDateTime(purchase.getPurchaseDateTime());
            existingPurchase.setPrice(purchase.getPrice());
            existingPurchase.setUser(purchase.getUser());
            existingPurchase.setStatus(purchase.getStatus());
        }

        savePurchasesToFile();
        return existingPurchase;
    }

    public Purchase savePurchase(Purchase purchase) {
        purchase.setId(generateNewId());
        purchase.setPurchaseDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        purchases.put(purchase.getId(), purchase);
        savePurchasesToFile();
        return purchase;
    }


    public boolean deletePurchase(String id) {
        if (purchases.containsKey(id)) {
            purchases.remove(id);
            savePurchasesToFile();
            return true; // Successfully deleted
        } else {
            return false; // Object with given ID not found
        }
    }

    private String generateNewId() {
        int newId = purchases.keySet().stream()
                .mapToInt(id -> Integer.parseInt(id))
                .max()
                .orElse(0) + 1;
        return String.valueOf(newId);
    }

    private void loadPurchases(String filePath) {
        BufferedReader in = null;
        purchases.clear();
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
                
                List<Coco> chocolates = new ArrayList<>();
                String chocolateIds = st.nextToken().trim();
                String[] chocolateIdsArray = chocolateIds.split(",");
                for (String cocoId : chocolateIdsArray) {
                    Coco coco = cocoDAO.findCoco(cocoId);
                    if (coco != null) {
                        chocolates.add(coco);
                    } else {
                        // Handle case where chocolate with ID cocoId is not found
                        // Depending on your business logic, you may log an error or skip it
                        System.err.println("Chocolate with ID " + cocoId + " not found.");
                    }
                }

                Factory factory = new Factory(); // Implement loading factory details
                String factoryId = st.nextToken().trim();
                factory.setId(factoryId);

                String purchaseDateTime = st.nextToken().trim();

                double price = Double.parseDouble(st.nextToken().trim());

                String buyerId = st.nextToken().trim();
                User buyer = userDAO.getUserById(buyerId);
                if (buyer == null) {
                    // Handle case where buyer with ID buyerId is not found
                    // Depending on your business logic, you may log an error or skip it
                    System.err.println("Buyer with ID " + buyerId + " not found.");
                    // Create a default user or handle as needed
                    buyer = new User();
                }

                String status = st.nextToken().trim();

                Purchase purchase = new Purchase(id, chocolates, factory, purchaseDateTime, price, buyer, status);
                purchases.put(id, purchase);
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

    private void savePurchasesToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation))) {
            for (Purchase purchase : purchases.values()) {
                out.write(purchase.getId() + ";");

                // Prolazak kroz čokolade
                List<Coco> chocolates = purchase.getChocolates();
                for (int i = 0; i < chocolates.size(); i++) {
                    Coco chocolate = chocolates.get(i);
                    out.write(chocolate.getId());
                    if (i < chocolates.size() - 1) {
                        out.write(",");
                    }
                }
                out.write(";");

                // Fabrika
                out.write(purchase.getFactory().getId() + ";");

                // Datum i vreme kupovine
                out.write(purchase.getPurchaseDateTime() + ";");

                // Cena
                out.write(purchase.getPrice() + ";");

                // Kupac
                out.write(purchase.getUser().getId() + ";");

                // Status
                out.write(purchase.getStatus());

                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<Purchase> searchPurchases(String factoryName, Double minPrice, Double maxPrice, String startDate, String endDate) {
        List<Purchase> result = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startDateTime = startDate != null ? LocalDateTime.parse(startDate, formatter) : null;
        LocalDateTime endDateTime = endDate != null ? LocalDateTime.parse(endDate, formatter) : null;

        for (Purchase purchase : purchases.values()) {
            boolean matches = true;
            
            // Search by factory name
            if (factoryName != null) {
                Factory f = purchase.getFactory();
                Factory factory = factoryDAO.findFactory(f.getId());
                if (factory != null && factory.getName() != null) {
                    if (!factory.getName().toLowerCase().contains(factoryName.toLowerCase())) {
                        matches = false;
                    }
                } else {
                    matches = false;
                }
            }

            // Search by price range
            if (matches && (minPrice != null || maxPrice != null)) {
                Double price = purchase.getPrice();
                if ((minPrice != null && price < minPrice) || (maxPrice != null && price > maxPrice)) {
                    matches = false;
                }
            }

            // Search by date range
            if (matches && (startDateTime != null || endDateTime != null)) {
                LocalDateTime purchaseDateTime = LocalDateTime.parse(purchase.getPurchaseDateTime(), formatter);
                if ((startDateTime != null && purchaseDateTime.isBefore(startDateTime)) ||
                    (endDateTime != null && purchaseDateTime.isAfter(endDateTime))) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(purchase);
            }
        }

        return result;
    }
	
    public List<Purchase> sortPurchases(String criterion, boolean ascending) {
        List<Purchase> sortedPurchases = new ArrayList<>(purchases.values());

        System.out.println("Sortiranje prema kriterijumu: " + criterion);

        Comparator<Purchase> comparator;

        switch (criterion.toLowerCase()) {
            case "factoryname": // Pazite da je mala slova
                comparator = Comparator.comparing(purchase -> {
                    Factory factory = factoryDAO.findFactory(purchase.getFactory().getId());
                    return factory != null ? factory.getName() : "";
                });
                break;
            case "price":
                comparator = Comparator.comparingDouble(Purchase::getPrice);
                break;
            case "date":
                comparator = Comparator.comparing(Purchase::getPurchaseDateTime);
                break;
            default:
                throw new IllegalArgumentException("Nepoznat kriterijum za sortiranje: " + criterion);
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        Collections.sort(sortedPurchases, comparator);
        return sortedPurchases;
    }
}
