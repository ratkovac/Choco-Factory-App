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

import beans.Cart;
import dao.CartDAO;

@Path("/carts")
public class CartService {

    @Context
    ServletContext ctx;

    public CartService() {
    }

    @PostConstruct
    public void init() {
        if (ctx.getAttribute("cartDAO") == null) {
            String contextPath = ctx.getRealPath("");
            ctx.setAttribute("cartDAO", new CartDAO(contextPath));
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Cart> getCarts() {
        CartDAO dao = (CartDAO) ctx.getAttribute("cartDAO");
        return dao.findAll();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Cart newCart(Cart cart) {
        CartDAO dao = (CartDAO) ctx.getAttribute("cartDAO");
        System.out.println("EEEE");
        System.out.println(cart.getUserId() + "OOO");
        System.out.println(cart.getTotalPrice() + "CENa");
        return dao.saveCart(cart);
    }
}
