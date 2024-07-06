package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import beans.Factory;
import beans.Location;

public class FactoryDAO {
	
	private HashMap<String, Factory> factories = new HashMap<>();
	private CocoDAO cocoDAO;
	private LocationDAO locationDAO;
	private String fileLocation;
	
	public FactoryDAO() {
	}
	
	public FactoryDAO(String contextPath) {
		System.out.println("OVO je context " + contextPath);
		this.fileLocation = "C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\factories.csv";
		System.out.println("Ovo je putanja" + fileLocation);
		this.cocoDAO = new CocoDAO(contextPath);
		this.locationDAO = new LocationDAO("C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\locations.csv");
		System.out.println("EE");
		loadFactories(fileLocation);
	}

	public Collection<Factory> findAll() {
		System.out.println("Ovde velicina " + factories.size());
	    // Ažuriranje statusa svake fabrike
	    for (Factory factory : factories.values()) {
	        factory.setStatus(checkStatus(factory.getWorkingTime()));
	        factory.setLocation(locationDAO.getLocationById(factory.getLocationId()));
	    }

	    // Sortiranje fabrika po statusu (Work > Do not work)
	    List<Factory> factoryList = new ArrayList<>(factories.values());
	    Collections.sort(factoryList, new Comparator<Factory>() {
	        @Override
	        public int compare(Factory f1, Factory f2) {
	            String status1 = f1.getStatus();
	            String status2 = f2.getStatus();

	            if (status1.equals("Work") && !status2.equals("Work")) {
	                return -1; 
	            } else if (!status1.equals("Work") && status2.equals("Work")) {
	                return 1; 
	            } else {
	                return 0;
	            }
	        }
	    });
	    System.out.println(factoryList.size() + "Finalna");
	    return factoryList;
	}

	public Factory findFactory(String id) {
		System.out.println(fileLocation);
		loadFactories(fileLocation);
		System.out.println("Ovo je id: " + id + "velicina:" + factories.size());
	    Factory factory = factories.get(id);
	    if (factory != null) {
	        factory.setStatus(checkStatus(factory.getWorkingTime()));
	    }
	    return factory;
	}

	public Factory updateFactory(String id, Factory factory) {
		Factory f = factories.containsKey(id) ? factories.get(id) : null;
		if (f == null) {
			return save(factory);
		} else {
			f.setName(factory.getName());
			f.setStatus(factory.getStatus());
			f.setLocationId(factory.getLocationId());
			f.setPathToLogo(factory.getPathToLogo());
			f.setRate(factory.getRate());
			f.setDeleted(factory.isDeleted());
			f.setWorkingTime(factory.getWorkingTime());
		}
		
		return f;
	}
	
	public Factory save(Factory factory) {
	    factory.setId(generateNewId());
	    factories.put(factory.getId(), factory);
	    System.out.println("Ovde je dobro" + factory.getId() + " " + factory.getPathToLogo());
	    System.out.println(factory);
	    saveFactoriesToFile();
	    return factory;
	}

	private String generateNewId() {
	    int newId = factories.keySet().stream()
	        .mapToInt(id -> Integer.parseInt(id))
	        .max()
	        .orElse(0) + 1;
	    return String.valueOf(newId);
	}
	
	public boolean deleteFactory(String id) {
	    if (factories.containsKey(id)) {
	        factories.remove(id);
	        saveFactoriesToFile();
	        return true; // Successfully deleted
	    } else {
	        return false; // Object with given ID not found
	    }
	}

	private void loadFactories(String filePath) {
		BufferedReader in = null;
		factories.clear();
		try {
			File file = new File(filePath);
			System.out.println(filePath + " je");
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", workingTime = "" , locationId = "", pathToLogo = "", rate = "", isDeleted = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				System.out.println(line + "linija");
			    line = line.trim();
				System.out.println(line + "linija2");
			    if (line.equals("") || line.indexOf('#') == 0)
			        continue;
			    st = new StringTokenizer(line, ";");
			    while (st.hasMoreTokens()) {
			    	System.out.println("Uslo u token");
			        id = st.nextToken().trim();
			        System.out.println("ID je: " + id);
			        name = st.nextToken().trim();
			        System.out.println("name je: " + name);
			        workingTime = st.nextToken().trim();
			        System.out.println("working je: " + workingTime);
			        locationId = st.nextToken().trim();
			        System.out.println("Location:" + locationId);
			        pathToLogo = st.nextToken().trim();
			        System.out.println("pathToLogo:" + pathToLogo);
			        rate = st.nextToken().trim();
			        System.out.println("rate:" + rate);
			        isDeleted = st.nextToken().trim();
			        System.out.println("isDeleted:" + isDeleted);
			        String workinTime = st.nextToken().trim(); // Dodato čitanje chocolateIds
			        System.out.println("workinTime:" + workinTime);
			        String chocolateIdsStr = st.nextToken().trim(); // Dodato čitanje chocolateIds
			        System.out.println(chocolateIdsStr);
			        ArrayList<Integer> chocolateIds = new ArrayList<>();
			        String[] chocolateIdsArray = chocolateIdsStr.split(",");
			        for (String chocolateId : chocolateIdsArray) {
			        	System.out.println("Idc: " + chocolateIdsStr);
			            chocolateIds.add(Integer.parseInt(chocolateId));
			        }
			        System.out.println(id + name + workingTime + locationId + pathToLogo + rate + isDeleted);
			        factories.put(id, new Factory(id, name, locationId, pathToLogo, Double.parseDouble(rate),
			                        Boolean.parseBoolean(isDeleted), workinTime, chocolateIds));
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( in != null ) {
				try {
					System.out.println("Velcina" + factories.size());
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	private void saveFactoriesToFile() {
	    try (BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation))) {
	    	for (Factory factory : factories.values()) {
	    	    String chocolateIdsStr = String.join(",", factory.getChocolateIds().stream().map(String::valueOf).toArray(String[]::new));
	    	    String line = String.join(";",
	    	            factory.getId(),
	    	            factory.getName(),
	    	            factory.getStatus(),
	    	            factory.getLocationId(),
	    	            factory.getPathToLogo(),
	    	            String.valueOf(factory.getRate()),
	    	            String.valueOf(factory.isDeleted()),
	    	            String.valueOf(factory.getWorkingTime()),
	    	            chocolateIdsStr
	    	    );
	    	    out.write(line);
	    	    out.newLine();
	    	}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public Factory addFactory(Factory factory) {
	    if (factory.getName() == null || factory.getName().trim().isEmpty()) {
	        factory.setName("Default Name");
	    }
	    /*if (factory.getLocation() == null || factory.getLocation().trim().isEmpty()) {
	        factory.setLocation("Default Location");
	    }*/
	    if (factory.getWorkingTime() == null || factory.getWorkingTime().trim().isEmpty()) {
	        factory.setWorkingTime("00:00-23:59");
	    }
	    
	    factory.setStatus("Do not work");
	    factory.setPathToLogo(factory.getPathToLogo() != null ? factory.getPathToLogo() : "");
	    factory.setRate(factory.getRate() != 0.0 ? factory.getRate() : 0.0);
        factory.setDeleted(factory.isDeleted());
        ArrayList<Integer> chocolateIds = new ArrayList<Integer>();
        chocolateIds.add(0);
        factory.setChocolateIds(
        	    factory.getChocolateIds() != null && !factory.getChocolateIds().isEmpty() ? factory.getChocolateIds() : new ArrayList<>(List.of(0))
        	);
        System.out.println(chocolateIds);
	    System.out.println("Velicina je: " + factories.size());
	    return save(factory);
	}
	
	public String checkStatus(String workingHours) {
	    String[] times = parseWorkingHours(workingHours);
	    LocalTime[] timeValues = getTimeValues(times);

	    if (isWithinWorkingHours(timeValues[0], timeValues[1], timeValues[2])) {
	        return "Work";
	    } else {
	        return "Do not work";
	    }
	}

	private static String[] parseWorkingHours(String workingHours) {
	    String[] times = workingHours.split("-");
	    if (times.length != 2) {
	        throw new IllegalArgumentException("Invalid working hours format. Expected format: 'HH:mm-HH:mm'");
	    }
	    return times;
	}

	private static LocalTime[] getTimeValues(String[] times) {
	    LocalTime[] timeValues = new LocalTime[3];
	    timeValues[0] = parseTime(times[0]);
	    timeValues[1] = parseTime(times[1]);
	    timeValues[2] = getCurrentTime();
	    return timeValues;
	}

	private static LocalTime parseTime(String time) {
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	    return LocalTime.parse(time, timeFormatter);
	}

	private static LocalTime getCurrentTime() {
	    return LocalTime.now();
	}

	private static boolean isWithinWorkingHours(LocalTime startTime, LocalTime endTime, LocalTime currentTime) {
	    return currentTime.isAfter(startTime) && currentTime.isBefore(endTime);
	}

	public List<Factory> searchFactories(String factoryName, String chocolateName, String address, Double averageRating, String chocolateCategory, String chocolateType, boolean isOpen) {
	    List<Factory> result = new ArrayList<>();

	    for (Factory factory : factories.values()) {
	        boolean matches = true;

	        if (factoryName != null && !factory.getName().equalsIgnoreCase(factoryName)) {
	            matches = false;
	        }

	        if (chocolateName != null && !factoryContainsChocolate(factory, chocolateName)) {
	            matches = false;
	        }

	        if (address != null) {
                String locationId = factory.getLocationId();
                Location location = locationDAO.getLocationById(locationId);
                if (location != null && location.getAddress() != null) {
                    if (!location.getAddress().toLowerCase().contains(address.toLowerCase())) {
                        matches = false;
                    }
                } else {
                    matches = false;
                }
            }

	        if (averageRating != null && factory.getRate() < averageRating) {
	            matches = false;
	        }

	        if (chocolateCategory != null && !factoryContainsChocolateWithCategory(factory, chocolateCategory)) {
	            matches = false;
	        }

	        if (chocolateType != null && !factoryContainsChocolateWithType(factory, chocolateType)) {
	            matches = false;
	        }

	        if (isOpen && !checkStatus(factory.getWorkingTime()).equals("Work")) {
	            matches = false;
	        }

	        if (matches) {
	            result.add(factory);
	        }
	    }

	    return result;
	}
	
	public boolean factoryContainsChocolateWithCategory(Factory factory, String category) {
	    List<String> chocolateIds = cocoDAO.findChocolateIdsByCategory(category);
	    
	    if (chocolateIds.isEmpty()) {
	        System.out.println("Nema čokolada sa kategorijom: " + category);
	        return false;
	    }
	    
	    for (String chocolateIdStr : chocolateIds) {
	        Integer chocolateId;
	        try {
	            chocolateId = Integer.parseInt(chocolateIdStr);
	        } catch (NumberFormatException e) {
	            System.out.println("Greška prilikom parsiranja ID-ja čokolade: " + chocolateIdStr);
	            return false;
	        }
	        
	        if (factory.getChocolateIds().contains(chocolateId)) {
	            if (factory.isDeleted()) {
	                System.out.println("Fabrika je označena kao obrisana.");
	                return false;
	            }
	            return true;
	        }
	    }

	    System.out.println("Fabrika ne sadrži čokoladu sa odgovarajućom kategorijom.");
	    return false;
	}

	public boolean factoryContainsChocolateWithType(Factory factory, String type) {
	    List<String> chocolateIds = cocoDAO.findChocolateIdsByType(type);
	    
	    if (chocolateIds.isEmpty()) {
	        System.out.println("Nema čokolada sa tipom: " + type);
	        return false;
	    }
	    
	    for (String chocolateIdStr : chocolateIds) {
	        Integer chocolateId;
	        try {
	            chocolateId = Integer.parseInt(chocolateIdStr);
	        } catch (NumberFormatException e) {
	            System.out.println("Greška prilikom parsiranja ID-ja čokolade: " + chocolateIdStr);
	            return false;
	        }
	        
	        if (factory.getChocolateIds().contains(chocolateId)) {
	            if (factory.isDeleted()) {
	                System.out.println("Fabrika je označena kao obrisana.");
	                return false;
	            }
	            return true;
	        }
	    }

	    System.out.println("Fabrika ne sadrži čokoladu sa odgovarajućim tipom.");
	    return false;
	}


	public boolean factoryContainsChocolate(Factory factory, String chocolateName) {
	    if (chocolateName == null || chocolateName.trim().isEmpty()) {
	        return false;
	    }
	    
	    String chocolateIdStr = cocoDAO.findChocolateIdByName(chocolateName);

	    if (chocolateIdStr == null) {
	        System.out.println("Čokolada nije pronađena za ime: " + chocolateName);
	        return false;
	    }

	    Integer chocolateId;

	    try {
	        chocolateId = Integer.parseInt(chocolateIdStr);
	    } catch (NumberFormatException e) {
	        System.out.println("Greška prilikom parsiranja ID-ja čokolade: " + chocolateIdStr);
	        return false;
	    }

	    if (!factory.getChocolateIds().contains(chocolateId)) {
	        System.out.println("Fabrika ne sadrži čokoladu sa ID-jem: " + chocolateId);
	        return false;
	    }

	    if (factory.isDeleted()) {
	        System.out.println("Fabrika je označena kao obrisana.");
	        return false;
	    }

	    return true;
	}

	public List<Factory> sortFactories(String criterion, boolean ascending) {
        List<Factory> sortedFactories = new ArrayList<>(factories.values());

        Comparator<Factory> comparator;

        switch (criterion.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(Factory::getName);
                break;
            case "address":
                comparator = Comparator.comparing(factory -> {
                    Location location = locationDAO.getLocationById(factory.getLocationId());
                    return location != null ? location.getAddress() : "";
                });
                break;
            case "rate":
                comparator = Comparator.comparingDouble(Factory::getRate);
                break;
            default:
                throw new IllegalArgumentException("Nepoznat kriterijum za sortiranje: " + criterion);
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        Collections.sort(sortedFactories, comparator);
        return sortedFactories;
    }

}
