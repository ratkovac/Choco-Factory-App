package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Comment;

public class CommentDAO {
	
	private HashMap<String, Comment> comments = new HashMap<String, Comment>();
    private String fileLocation;
    
    public CommentDAO() {
    	
    }
    
    public CommentDAO(String fileLocation) {
        fileLocation = "C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\comments.csv";
        System.out.println("Ovo je file loc comments: " + fileLocation);
        loadCommentsFromFile(fileLocation);
    }
    
    public void addComment(Comment comment) {
        comments.put(comment.getId(), comment);
        saveCommentsToFile();
    }
    
    public Comment getCommentById(String id) {
        return comments.get(id);
    }
    
    public Collection<Comment> getAllComments() {
        return comments.values();
    }
    
    private void loadCommentsFromFile(String filePath) {
        BufferedReader in = null;
        comments.clear();
        try {
            File file = new File(filePath);
            System.out.println(filePath + " je");
            in = new BufferedReader(new FileReader(file));
            String line, id = "", text = "", factoryId = "";
            StringTokenizer st;
            while ((line = in.readLine()) != null) {
                System.out.println(line + " linija");
                line = line.trim();
                System.out.println(line + " linija2");
                if (line.equals("") || line.indexOf('#') == 0)
                    continue;
                st = new StringTokenizer(line, ";");
                while (st.hasMoreTokens()) {
                    System.out.println("Uslo u token");
                    id = st.nextToken().trim();
                    System.out.println("ID je: " + id);
                    text = st.nextToken().trim();
                    System.out.println("Text je: " + text);
                    factoryId = st.nextToken().trim();
                    System.out.println("Factory ID je: " + factoryId);

                    comments.put(id, new Comment(id, text, factoryId));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    System.out.println("Velcina " + comments.size());
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    private void saveCommentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation))) {
            for (Comment comment : comments.values()) {
                writer.write(comment.getId() + ";" + comment.getText() + ";" + comment.getFactoryId());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Comment> GetCommentsByFactory(String factoryId) {
    	System.out.println("OVO JE TO" + comments.size() + "Borj id:" + factoryId);
    	ArrayList<Comment> comms = new ArrayList<Comment>();
        for(Comment coco : comments.values()) {
            System.out.println("Checking comment with factoryId: " + coco.getFactoryId() + " against " + factoryId);
        	if(coco.getFactoryId().equals(factoryId)) {
        		comms.add(coco);
        	}
        }
        return comms;
    }
}