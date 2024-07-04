package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Purchase;
import dao.PurchaseDAO;

@Path("/purchases")
public class PurchaseService {

    @Context
    ServletContext ctx;

    public PurchaseService() {
    }

    @PostConstruct
    public void init() {
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

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Purchase newPurchase(Purchase purchase) {
        PurchaseDAO dao = (PurchaseDAO) ctx.getAttribute("purchaseDAO");
        return dao.savePurchase(purchase);
    }
}
