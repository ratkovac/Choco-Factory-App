package services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Factory;
import dao.FactoryDAO;

@Path("/factories")
public class FactoryService {

    @Context
    ServletContext ctx;
    private FactoryDAO factoryDAO;

    @PostConstruct
    public void init() {
    	System.out.println("Pre");
        factoryDAO = (FactoryDAO) ctx.getAttribute("factoryDAO");
        if (factoryDAO == null) {
        	System.out.println("posle");
            factoryDAO = new FactoryDAO(ctx.getRealPath("/"));
            ctx.setAttribute("factoryDAO", factoryDAO);
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Factory> getFactories() {
        System.out.println("Pozvana je metoda getFactories()");
        ArrayList<Factory> fabrike = new ArrayList<>(factoryDAO.findAll());
        System.out.println(fabrike.size() + "velicina");
        return new ArrayList<>(factoryDAO.findAll());
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Factory addFactory(Factory factory) {
    	System.out.println("FAbrika");
    	System.out.println(factory);
    	System.out.println(factoryDAO.findAll().size() + " weq");
        return factoryDAO.addFactory(factory);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Factory getFactory(@PathParam("id") String id) {
        Factory factory = factoryDAO.findFactory(id);
        if (factory != null) {
            String status = factoryDAO.checkStatus(factory.getWorkingTime());
            factory.setStatus(status);
        }
        return factory;
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean deleteFactory(@PathParam("id") String id) {
        return factoryDAO.deleteFactory(id);
    }
    
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Factory updateFactory(@PathParam("id") String id, Factory updatedFactory) {
        return factoryDAO.updateFactory(id, updatedFactory);
    }
    @PUT
    @Path("/grade/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Factory updateGradeFactory(@PathParam("id") String id, String grade) {
    	System.out.println("OCENA-----------------------------------------------------------------------------------" + grade);
    	Factory factory = factoryDAO.findFactory(id);
    	double rate = factory.getRate();
    	rate += Double.parseDouble(grade);
    	factory.setRate(rate/2);
        return factoryDAO.updateFactory(id, factory);
    }
    
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Factory> searchFactories(@QueryParam("factoryName") String factoryName,
                                         @QueryParam("address") String address,
                                         @QueryParam("averageRating") Double averageRating,
                                         @QueryParam("chocolateName") String chocolateName,
                                         @QueryParam("chocolateType") String chocolateType,
                                         @QueryParam("chocolateCategory") String chocolateCategory,
                                         @QueryParam("isOpen") boolean isOpen) {
    	//return factoryDAO.searchFactories(factoryName, chocolateName, address, averageRating, chocolateCategory, chocolateType, isOpen);
        List<Factory> result = new ArrayList<>();

        for (Factory factory : factoryDAO.findAll()) {
        	factory.setLocation(factoryDAO.getLocationById(factory.getLocationId()));
            if ((factoryName == null || factoryName.trim().isEmpty() || factory.getName().toLowerCase().contains(factoryName.toLowerCase())) &&
                (address == null || address.trim().isEmpty() || factory.getLocation().getAddress().equalsIgnoreCase(address)) &&
                (chocolateName == null || chocolateName.trim().isEmpty() || factoryDAO.factoryContainsChocolate(factory, chocolateName)) &&
                (chocolateType == null || chocolateType.trim().isEmpty() || factoryDAO.factoryContainsChocolateWithType(factory, chocolateType)) &&
                (chocolateCategory == null || chocolateCategory.trim().isEmpty() || factoryDAO.factoryContainsChocolateWithCategory(factory, chocolateCategory)) &&
                (averageRating == null || factory.getRate() >= averageRating) &&
                (!isOpen || factoryDAO.checkStatus(factory.getWorkingTime()).equals("Work"))) {
            	
            	result.add(factory);
            }
        }

        return result;
    }

    @GET
    @Path("/sort")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Factory> sortFactories(@QueryParam("criterion") String criterion, @QueryParam("ascending") boolean ascending) {
        return factoryDAO.sortFactories(criterion, ascending);
    }

}
