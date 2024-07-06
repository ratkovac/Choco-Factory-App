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
import java.util.List;
import java.util.StringTokenizer;

import beans.Comment;

public class CommentDAO {
	
	private HashMap<String, Comment> comments = new HashMap<String, Comment>();
    private String fileLocation;
    
    public CommentDAO() {
    	
    }
    
    public CommentDAO(String fileLocation) {
        this.fileLocation = "C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\comments.csv";
        //this.fileLocation = "C:\\Users\\janic\\FAX\\SEMESTAR 6\\Veb programiranje\\CocoFactory\\veb-projekat\\Backend\\WebShopAppREST\\src\\main\\webapp\\comments.csv";
        System.out.println("Ovo je file loc comments: " + fileLocation);
        loadCommentsFromFile(this.fileLocation);
    }
    
    public Comment addComment(Comment comment) {
    	comment.setId(generateNewId());
		System.out.println("DODAVANJE KOMENTARA --------------------------------------------------------3-");
        comments.put(comment.getId(), comment);
    	System.out.println(comments.size());
    	for (Comment commentt : comments.values()) {
	    }
        saveCommentsToFile();
        return comment;
    }
    
    private String generateNewId() {
    	System.out.println(comments.size());
        int newId = comments.keySet().stream()
                .mapToInt(id -> Integer.parseInt(id))
                .max()
                .orElse(0) + 1;
        return String.valueOf(newId);
    }
    
    public Comment getCommentById(String id) {
        return comments.get(id);
    }
    
    public Collection<Comment> getAllComments() {
        return comments.values();
    }
    
    public Collection<Comment> getValidComments() {
        List<Comment> validComments = new ArrayList<>();
        for (Comment comment : comments.values()) {
            if (comment.isValid()) {
                validComments.add(comment);
            }
        }
        return validComments;
    }
    
    public Comment updateComment(String id, Comment comment) {
        Comment existingComment = comments.containsKey(id) ? comments.get(id) : null;
        if (existingComment == null) {
            return addComment(comment);
        } else {
            existingComment.setId(comment.getId());
            existingComment.setText(comment.getText());
            existingComment.setValid(comment.isValid());
            existingComment.setFactoryId(comment.getFactoryId());
            // Dodajte ostala polja koja želite ažurirati
        }

        saveCommentsToFile();
        return existingComment;
    }
    
    private void loadCommentsFromFile(String filePath) {
        BufferedReader in = null;
        comments.clear();
        try {
            File file = new File(filePath);
            System.out.println(filePath + " je");
            in = new BufferedReader(new FileReader(file));
            String line, id = "", text = "", factoryId = "", isValid ="";
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
                    isValid = st.nextToken().trim();
                    System.out.println("Factory ID je: " + factoryId);

                    comments.put(id, new Comment(id, text, factoryId, Boolean.parseBoolean(isValid)));
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

    
    private void saveCommentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation))) {
            for (Comment comment : comments.values()) {
                writer.write(comment.getId() + ";" + comment.getText() + ";" + comment.getFactoryId() + ";" + comment.isValid());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Comment> GetCommentsByFactory(String factoryId) {
    	ArrayList<Comment> comms = new ArrayList<Comment>();
        for(Comment coco : comments.values()) {
        	if(coco.getFactoryId().equals(factoryId)) {
        		comms.add(coco);
        	}
        }
        return comms;
    }
    
    public ArrayList<Comment> GetValidCommentsByFactory(String factoryId) {
    	ArrayList<Comment> comms = new ArrayList<Comment>();
        for(Comment coco : comments.values()) {
        	if(coco.getFactoryId().equals(factoryId) && coco.isValid()) {
        		comms.add(coco);
        	}
        }
        return comms;
    }
}