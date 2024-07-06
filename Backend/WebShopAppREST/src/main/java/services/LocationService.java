package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Location;
import dao.LocationDAO;

@Path("/location")
public class LocationService {

    @Context
    ServletContext ctx;
    private LocationDAO locationDAO;

    @PostConstruct
    public void init() {
    	locationDAO = (LocationDAO) ctx.getAttribute("locationDAO");
        if(ctx.getAttribute("locationDAO") == null) {
        	locationDAO = new LocationDAO(ctx.getRealPath("/"));
        	ctx.setAttribute("locationDAO", locationDAO);
        }
    }
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Location> getLocations() {
        return new ArrayList<>(locationDAO.getAllLocations());
    }

    @GET
    @Path("/{locationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Location getLocation(@PathParam("locationId") String locationId) {
        return locationDAO.getLocationById(locationId);
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Location addLocation(Location location) {
        LocationDAO dao = (LocationDAO) ctx.getAttribute("locationDAO");
        return dao.addLocation(location);
    }
}
