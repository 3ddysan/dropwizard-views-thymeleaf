package edu.thinktank.thymeleaf.example;

import io.dropwizard.views.View;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HomeView extends View {

    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                                              "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
    private final Home data;

    protected HomeView(final Home data) {
        super("home.html");
        this.data = data;
    }

    public List<String> getSentences() {
        return Arrays.asList(LOREM_IPSUM, LOREM_IPSUM, LOREM_IPSUM);
    }

    public Calendar getToday() {
        return data.getCalendar();
    }

}
