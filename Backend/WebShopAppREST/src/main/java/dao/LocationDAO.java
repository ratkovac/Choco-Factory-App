package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Location;

public class LocationDAO {
    
    private HashMap<String, Location> locations = new HashMap<>();
    private String fileLocation;
    
    public LocationDAO() {
    	
    }
    
    public LocationDAO(String fileLocation) {
    	this.fileLocation = "C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\locations.csv";
        loadLocationsFromFile(this.fileLocation);
    }
    
    public Location addLocation(Location location) {
        location.setId(generateNewId());
        locations.put(location.getId(), location);
        saveLocationsToFile();
        return location;
    }
    
    private String generateNewId() {
        int newId = locations.keySet().stream()
                .mapToInt(id -> Integer.parseInt(id))
                .max()
                .orElse(0) + 1;
        return String.valueOf(newId);
    }
    
    public Location getLocationById(String id) {
        return locations.get(id);
    }
    
    public Collection<Location> getAllLocations() {
        return locations.values();
    }
    
    private void loadLocationsFromFile(String filePath) {
        BufferedReader in = null;
        locations.clear();
        try {
            File file = new File(filePath);
            in = new BufferedReader(new FileReader(file));
            String line, id = "", longitude = "", latitude = "", address = "";
            StringTokenizer st;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equals("") || line.indexOf('#') == 0)
                    continue;
                st = new StringTokenizer(line, ";");
                while (st.hasMoreTokens()) {
                    id = st.nextToken().trim();
                    longitude = st.nextToken().trim();
                    latitude = st.nextToken().trim();
                    address = st.nextToken().trim();
                    
                    locations.put(id, new Location(id, longitude, latitude, address));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void saveLocationsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation))) {
            for (Location location : locations.values()) {
                writer.write(location.getId() + ";" + location.getLongitude() + ";" + location.getLatitude() + ";" + location.getAddress());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
