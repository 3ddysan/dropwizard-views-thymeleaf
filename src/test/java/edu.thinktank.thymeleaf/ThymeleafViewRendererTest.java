package edu.thinktank.thymeleaf;

import com.codahale.metrics.MetricRegistry;
import edu.thinktank.thymeleaf.views.AbsoluteView;
import edu.thinktank.thymeleaf.views.BadView;
import edu.thinktank.thymeleaf.views.ErrorView;
import edu.thinktank.thymeleaf.views.RelativeView;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.views.ViewMessageBodyWriter;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ThymeleafViewRendererTest extends JerseyTest {

    public static final int ERROR_STATUSCODE = 500;

    static {
        BootstrapLogging.bootstrap();
    }

    @Path("/test/")
    @Produces(MediaType.TEXT_HTML)
    public static class ExampleResource {

        @GET
        @Path("/absolute")
        public AbsoluteView showAbsolute() {
            return new AbsoluteView("yay");
        }

        @GET
        @Path("/relative")
        public RelativeView showRelative() {
            return new RelativeView();
        }

        @GET
        @Path("/bad")
        public BadView showBad() {
            return new BadView();
        }

        @GET
        @Path("/error")
        public ErrorView showError() {
            return new ErrorView();
        }
    }

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        final ResourceConfig config = new ResourceConfig();
        final ThymeleafViewRenderer renderer = new ThymeleafViewRenderer();
        renderer.configure(new HashMap<>());
        config.register(new ViewMessageBodyWriter(new MetricRegistry(), ImmutableList.of(renderer)));
        config.register(new ExampleResource());
        return config;
    }

    @Test
    public void rendersViewsWithAbsoluteTemplatePaths() throws Exception {
        final String response = target("/test/absolute").request().get(String.class);
        assertThat(response).isEqualTo("<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title>Hello Thymeleaf</title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<p>yay</p>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>");
    }

    @Test
    public void rendersViewsWithRelativeTemplatePaths() throws Exception {
        final String response = target("/test/relative").request().get(String.class);
        assertThat(response).isEqualTo("<html>\n" +
                "\n" +
                "<head>\n" +
                "    <title>Hello Thymeleaf</title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<p>relative view</p>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>");
    }

    @Test
    public void returnsA500ForViewsWithBadTemplatePaths() throws Exception {
        try {
            target("/test/bad").request().get(String.class);
            failBecauseExceptionWasNotThrown(WebApplicationException.class);
        } catch (final WebApplicationException e) {
            assertThat(e.getResponse().getStatus())
                    .isEqualTo(ERROR_STATUSCODE);

            assertThat(e.getResponse().readEntity(String.class))
                    .isEqualTo(ViewMessageBodyWriter.TEMPLATE_ERROR_MSG);
        }
    }

    //if example-error.html is missing still green
    @Test
    public void returnsA500ForViewsThatCantCompile() throws Exception {
        try {
            target("/test/error").request().get(String.class);
            failBecauseExceptionWasNotThrown(WebApplicationException.class);
        } catch (final WebApplicationException e) {
            assertThat(e.getResponse().getStatus())
                    .isEqualTo(ERROR_STATUSCODE);

            assertThat(e.getResponse().readEntity(String.class))
                    .isEqualTo(ViewMessageBodyWriter.TEMPLATE_ERROR_MSG);
        }
    }

}
