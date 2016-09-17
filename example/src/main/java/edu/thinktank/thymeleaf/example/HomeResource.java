package edu.thinktank.thymeleaf.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/home")
@Produces(MediaType.TEXT_HTML)
public class HomeResource {

    @GET
    public HomeView home() {
        final Home data = new Home();
        return new HomeView(data);
    }

}
