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

import beans.Coco;

public class CocoDAO {
	
	private HashMap<String, Coco> cocos = new HashMap<String, Coco>();
	private String FileLocation;
	
	public CocoDAO() {
		
	}
	
	public CocoDAO(String contextPath) {
		FileLocation = contextPath + "/cocolates.csv";
		LoadCocos(contextPath);
	}

	public Collection<Coco> FindAll() {
		return cocos.values();
	}

	public Coco FindCoco(String id) {
		return cocos.containsKey(id) ? cocos.get(id) : null;
	}
	
	public Coco UpdateCoco(String id, Coco coco) {
		Coco c = cocos.containsKey(id) ? cocos.get(id) : null;
		if (c == null) {
			return Save(coco);
		} else {
			c.setName(coco.getName());
			c.setCost(coco.getCost());
			c.setCategory(coco.getCategory());
			c.setType(coco.getType());
			c.setStatus(coco.getStatus());
			c.setMass(coco.getMass());
			c.setDetails(coco.getDetails());
			c.setPicture(coco.getPicture());
			c.setStock(coco.getStock());
			c.setDeleted(coco.isDeleted());
		}
		
		return c;
	}
	
	public Coco Save(Coco coco) {
	    coco.setId(GenerateNewId());
	    cocos.put(coco.getId(), coco);
	    SaveChocolatesToFile();
	    return coco;
	}

	private String GenerateNewId() {
	    int newId = cocos.keySet().stream()
	        .mapToInt(id -> Integer.parseInt(id))
	        .max()
	        .orElse(0) + 1;
	    return String.valueOf(newId);
	}
	
	public boolean DeleteCoco(String id) {
	    if (cocos.containsKey(id)) {
	        cocos.remove(id);
	        SaveChocolatesToFile();
	        return true; // Uspesno obrisano
	    } else {
	        return false; // Objekat sa datim ID-om nije pronadjen
	    }
	}


	private void LoadCocos(String contextPath) {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/cocolates.txt");
			in = new BufferedReader(new FileReader(file));
			String line, id = "", name = "", cost = "", category = "", type = "", status = "", mass = "", details = "", picture = "", stock = "", isDeleted = "";
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					name = st.nextToken().trim();
					cost = st.nextToken().trim();
					category = st.nextToken().trim();
					type = st.nextToken().trim();
					status = st.nextToken().trim();
					mass = st.nextToken().trim();
					details = st.nextToken().trim();
					picture = st.nextToken().trim();
					stock = st.nextToken().trim();
					isDeleted = st.nextToken().trim();
				}
				cocos.put(id, new Coco(id, name, Double.parseDouble(cost), category, type, status, Double.parseDouble(mass), details, picture, Integer.parseInt(stock), Boolean.parseBoolean(isDeleted)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
		
	}
	
	private void SaveChocolatesToFile() {
	    try (BufferedWriter out = new BufferedWriter(new FileWriter(FileLocation))) {
	        for (Coco cocolates : cocos.values()) {
	            String line = String.join(";",
	                    cocolates.getId(),
	                    cocolates.getName(),
	                    String.valueOf(cocolates.getCost()),
	                    cocolates.getType(),
	                    cocolates.getCategory(),
	                    String.valueOf(cocolates.getMass()),
	                    cocolates.getDetails(),
	                    cocolates.getPicture(),
	                    String.valueOf(cocolates.isDeleted())
	            );
	            out.write(line);
	            out.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
