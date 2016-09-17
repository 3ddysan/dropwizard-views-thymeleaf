package edu.thinktank.thymeleaf.example;

import com.google.common.collect.ImmutableList;
import edu.thinktank.thymeleaf.ThymeleafViewRenderer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.Map;

public class ExampleApp extends Application<ExampleConfig> {

    @Override
    public void initialize(final Bootstrap<ExampleConfig> bootstrap) {
        bootstrap.addBundle(new ViewBundle<ExampleConfig>(ImmutableList.of(new ThymeleafViewRenderer())) {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(final ExampleConfig configuration) {
                return configuration.views;
            }
        });
    }

    @Override
    public void run(final ExampleConfig exampleConfig, final Environment environment) {
        environment.jersey().register(new HomeResource());
    }

    public static void main(final String[] args) throws Exception {
        new ExampleApp().run(args);
    }

}
