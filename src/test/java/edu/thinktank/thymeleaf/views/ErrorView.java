package edu.thinktank.thymeleaf.views;

import io.dropwizard.views.View;

public class ErrorView extends View {
    public ErrorView() {
        super("/example-error.html");
    }
}
