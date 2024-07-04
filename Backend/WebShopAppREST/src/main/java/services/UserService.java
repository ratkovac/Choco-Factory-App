package services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Factory;
import beans.User;
import beans.UserCredentials;
import beans.UserRegistration;
import dao.FactoryDAO;
import dao.UserDAO;
import enums.UserRole;
import enums.UserStatus;

@Path("/user")
public class UserService {
    @Context
    ServletContext ctx;
    private UserDAO userDAO;
    private FactoryDAO factoryDAO;
    
    @PostConstruct
    public void init() {
        userDAO = (UserDAO) ctx.getAttribute("userDAO");
        if (userDAO == null) {
            userDAO = new UserDAO(ctx.getRealPath("/"));
            ctx.setAttribute("userDAO", userDAO);
        }
        factoryDAO = new FactoryDAO(ctx.getRealPath("/"));
    }
    
    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") String id, User updatedUser) {
        UserDAO dao = (UserDAO) ctx.getAttribute("userDAO");
        System.out.println("Korisnik se updejtuje");
        Boolean b = dao.updateUserForm(id, updatedUser);
        if (b) {
            System.out.println("Korisnik updejtovan");
            return Response.ok().build();
        } else {
            System.out.println("Korisnik NIJE updejtovan");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @PUT
    @Path("/update/manager/{factoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateManager(@PathParam("factoryId") String factoryId, @QueryParam("managerId") String managerId) {
        UserDAO dao = (UserDAO) ctx.getAttribute("UserDAO");
        System.out.println("Korisnik se updejtuje---------------------------------------------");
        System.out.println("FactoryId: " + factoryId);
        System.out.println("managerId:" + managerId);
        User user = userDAO.getUserById(managerId);
        System.out.println(user.getFirstName());
        Boolean b = userDAO.updateManagerForm(factoryId, user, factoryDAO);
        if (b) {
            System.out.println("Korisnik updejtovan");
            return Response.ok().build();
        } else {
            System.out.println("Korisnik NIJE updejtovan");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("/profile/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") String id) {
        UserDAO dao = (UserDAO) ctx.getAttribute("UserDAO");
        User user = dao.getUserById(id);
        if (user != null) {
            return user;
        } else {
            System.out.println("An error occurred while finding the user");
            return null;
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@Context HttpServletRequest request, UserCredentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        User user = userDAO.getRegisteringUser(username, password);

        if (user != null) {
            // Postavljanje korisnika u sesiju
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            return Response.ok().entity(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User does not exist with username: " + username).build();
        }
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserRegistration getUserRegById(@PathParam("id") String id) {
        System.out.println("Trazi korisnika po id");
        UserDAO dao = (UserDAO) ctx.getAttribute("UserDAO");
        UserRegistration user = dao.getUserRegistrationById(id);
        if (user != null) {
            System.out.println("User found");
            return user;
        } else {
            System.out.println("An error occurred while finding the user");
            return null;
        }
    }
    
    @POST
    @Path("/register/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User registerUser(@PathParam("type") String type, UserRegistration userRegistration) {
        System.out.println("Registrovanje korisnika u register servisu");
        System.out.println("TIP:" + type);
        System.out.println("Name: " + userRegistration.getName() + "LastName: " + userRegistration.getSurname() + "Rod:" + userRegistration.getGender());
        User user = userDAO.registerUser(userRegistration, type);
        if (user != null) {
            System.out.println("Korisnik JESTE registrovan");
            return user;
        } else {
            System.out.println("Korisnik NIJE registrovan");
            return null;
        }
    }
    
    @GET
    @Path("/allUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getAllUsers() {
        System.out.println("Pozvana je metoda getAllUsers()");
        ArrayList<User> users = new ArrayList<>(userDAO.findAll());
        System.out.println("Broj korisnika: " + users.size());
        return users;
    }
    
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> searchUsers(@QueryParam("firstName") String firstName,
                                  @QueryParam("lastName") String lastName,
                                  @QueryParam("username") String username,
                                  @QueryParam("role") UserRole role) {
    	return userDAO.searchUsers(firstName, lastName, username, role);
    }

    
    @GET
    @Path("/sort")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> sortUsers(@QueryParam("criterion") String criterion, @QueryParam("ascending") boolean ascending) {
        return userDAO.sortUsers(criterion, ascending);
    }
    
    @GET
    @Path("/allManagers")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<User> getAllManagers() {
        System.out.println("Menadzeri");
        return userDAO.getAllManagers(factoryDAO);
    }

    @GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUser(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                return Response.ok().entity(user).build();
            }
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity("No user is logged in.").build();
    }
    
    @PUT
    @Path("/toggleStatus/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleUserStatus(@PathParam("id") String id) {
        User user = userDAO.getUserById(id);

        if (user != null) {
            boolean newStatus = !user.isActive();
            user.setActive(newStatus);
            if (!newStatus) {
            	user.setStatus(UserStatus.DEACTIVATED);
            } else {
            	user.setStatus(UserStatus.ACTIVATED);
            }

            Boolean success = userDAO.updateUserForm(id, user);

            if (success) {
            	System.out.println("uspjesno izmijenjeno");
                return Response.ok().entity(user).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update user status.").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found with id: " + id).build();
        }
    }
}
