package services;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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

import beans.Coco;
import beans.Purchase;
import dao.CocoDAO;
import beans.User;
import dao.PurchaseDAO;
import enums.UserRole;

@Path("/purchases")
public class PurchaseService {

    @Context
    ServletContext ctx;
	private PurchaseDAO purchaseDAO;

    public PurchaseService() {
    }

    @PostConstruct
    public void init() {
    	purchaseDAO = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        if (ctx.getAttribute("purchaseDAO") == null) {
            String contextPath = ctx.getRealPath("");
            ctx.setAttribute("purchaseDAO", new PurchaseDAO(contextPath));
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Purchase> getPurchases() {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        return dao.findAll();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Purchase> getPurchasesByUser(@PathParam("id") String id) {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        dao = new PurchaseDAO("C:\\Users\\HP\\OneDrive\\Radna površina\\najnoviji web projekat\\CocoFactory\\Backend\\WebShopAppREST\\src\\main\\webapp\\purchases.csv");
        System.out.println("ID" + id);
        return dao.findAllByUser(id);
    }
    
    @GET
    @Path("/factory/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Purchase> getPurchasesByFactory(@PathParam("id") String id) {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        return dao.findAllByFactory(id);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Purchase newPurchase(Purchase purchase) {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        return dao.savePurchase(purchase);
    }
    
    @PUT
    @Path("/cancel/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Purchase updatePurchase(@PathParam("id") String id) {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        Purchase purchase = dao.findPurchase(id);
        purchase.setStatus("Otkazano");
        return dao.updatePurchase(id, purchase);
    }
    
    @PUT
    @Path("/accept/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Purchase updatePurchaseAccept(@PathParam("id") String id) {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        System.out.println("=====================================================================");
        System.out.println(id);
        Purchase pur = dao.findPurchase(id);
        System.out.println("=====================================================================");
        System.out.println(pur.getChocolates().size() + " COKOLADE");
        pur.setStatus("Odobreno");
        return dao.updatePurchase(id, pur);
    }
    
    @PUT
    @Path("/reject/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Purchase updatePurchaseReject(@PathParam("id") String id) {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        System.out.println("=====================================================================");
        System.out.println(id);
        Purchase pur = dao.findPurchase(id);
        System.out.println("=====================================================================");
        System.out.println(pur.getChocolates().size() + " COKOLADE");
        pur.setStatus("Odbijeno");
        return dao.updatePurchase(id, pur);
    }
    
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Purchase> searchPurchases(@QueryParam("factoryName") String factoryName,
    									  @QueryParam("minPrice") Double minPrice, @QueryParam("maxPrice") Double maxPrice,
    									  @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
    	PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
    	return dao.searchPurchases(factoryName, minPrice, maxPrice, startDate, endDate);
    }
    
    @GET
    @Path("/sort")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Purchase> sortPurchases(@QueryParam("criterion") String criterion, @QueryParam("ascending") boolean ascending) {
    	PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        return dao.sortPurchases(criterion, ascending);
    }
    
}
