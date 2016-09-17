package edu.thinktank.thymeleaf.views;

import io.dropwizard.views.View;

import java.util.Arrays;
import java.util.List;

public class AbsoluteView extends View {
    private final String name;

    public AbsoluteView(final String name) {
        super("/example.html");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return "Hello " + name;
    }

    public List<String> names(){
        return Arrays.asList(name);
    }
}
