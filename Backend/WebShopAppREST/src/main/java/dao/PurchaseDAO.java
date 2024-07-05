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
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Cart;
import beans.Coco;
import beans.CocoInCart;
import beans.Factory;
import beans.Purchase;
import beans.User;

public class PurchaseDAO {

    private HashMap<String, Purchase> purchases = new HashMap<>();
    private String fileLocation;
    private CocoInCartDAO cocoInCartDAO; // DAO za čokolade u korpi
    private CocoDAO cocoDAO;
    private UserDAO userDAO; // DAO za korisnike (kupce)

    public PurchaseDAO(String contextPath) {
        this.fileLocation = "C:\\Users\\janic\\FAX\\SEMESTAR 6\\Veb programiranje\\CocoFactory\\veb-projekat\\Backend\\WebShopAppREST\\src\\main\\webapp\\purchases.csv"; // Podesite putanju do CSV fajla
        cocoInCartDAO = new CocoInCartDAO(contextPath); // Inicijalizacija DAO za čokolade u korpi
        userDAO = new UserDAO(contextPath); // Inicijalizacija DAO za korisnike (kupce)
        cocoDAO = new CocoDAO(contextPath);
        loadPurchases(fileLocation); // Učitavanje postojećih kupovina prilikom inicijalizacije
        countCancelled();
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
    	System.out.println("EEEE");
    	System.out.println(purchases.size());
        return purchases.get(id);
    }

    public Purchase updatePurchase(String id, Purchase purchase) {
    	System.out.println("USLOOO------------------------------------------------------------USLO");
    	Purchase pur = findPurchase(id);
    	System.out.println(pur.getStatus());
    	if(pur.getStatus().equals("Odobreno")) {
    		updateFactory(pur);
    	}
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
        int canceled = findCancelledPurchasesInLastMonth(purchase.getUser().getId());
        purchase.getUser().setCanceled(canceled);
        userDAO.updateUserForm(purchase.getUser().getId(), purchase.getUser());
        return existingPurchase;
    }
    
    public int findCancelledPurchasesInLastMonth(String userId) {
        // Datum pre mesec dana od danasnjeg datuma
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // Pronađi sve purchase za korisnika sa datim ID
        List<Purchase> purchases = findAllByUser(userId);

        // Brojač za otkazane purchases u prethodnih mesec dana
        int counter = 0;

        // Iteriraj kroz sve purchase
        for (Purchase purchase : purchases) {
            // Konvertuj purchaseDateTime string u LocalDateTime
            LocalDateTime purchaseDateTime = LocalDateTime.parse(purchase.getPurchaseDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Proveri da li je purchase u prethodnih mesec dana i da li je status "Otkazana"
            if (purchaseDateTime.isAfter(oneMonthAgo) && purchase.getStatus().equals("Otkazano")) {
                counter++;
            }
        }

        return counter;
    }
    
    public void countCancelled() {
    	System.out.println("********************************************************************************************************************");
    	System.out.println("********************************************************************************************************************");
    	for(User user : userDAO.findAll()) {
    		int canceled = findCancelledPurchasesInLastMonth(user.getId());
    		user.setCanceled(canceled);
            userDAO.updateUserForm(user.getId(), user);  		
    	}
    }
    
    public void updateFactory(Purchase purchase) {
    	System.out.println("ODOBRAVANJE");
    	for(CocoInCart coco : purchase.getCartId()) {
    		Coco chocolateCoco = cocoDAO.findCoco(coco.getIdChocolate());
    		int current = chocolateCoco.getStock();
    		System.out.println("TRENUGNO: " + current);
    		System.out.println("SKIDA SE: " + coco.getQuantity());
    		chocolateCoco.setStock(current - coco.getQuantity());
    		System.out.println("Posle skidanja: " + chocolateCoco.getStock());
    		cocoDAO.updateCoco(chocolateCoco.getId(), chocolateCoco);
    	}
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
                String idsString = st.nextToken().trim(); // Ovde dobijate ceo string sa ID-ovima, recimo "ID,ID,Id,Id,Id..."
                String[] idsArray = idsString.split(","); // Razdvajanje stringa na niz ID-ova
                List<CocoInCart> chocolatess = new ArrayList<>();
                for (String ids : idsArray) {
                    // Sada možete raditi sa svakim ID-jem posebno
                    CocoInCart cart = cocoInCartDAO.findCocoInCart(ids); // Koristite trim() da biste uklonili moguće praznine oko ID-ova
                    chocolatess.add(cart);
                }

                Purchase purchase = new Purchase(id, chocolates, factory, purchaseDateTime, price, buyer, status, chocolatess);
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
                out.write(purchase.getStatus() + ";");
                
                List<CocoInCart> chocolatess = purchase.getCartId();
                for (int i = 0; i < chocolatess.size(); i++) {
                    CocoInCart chocolate = chocolatess.get(i);
                    out.write(chocolate.getId());
                    if (i < chocolatess.size() - 1) {
                        out.write(","); // Dodaj zarez osim nakon poslednjeg elementa
                    }
                }
;

                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
