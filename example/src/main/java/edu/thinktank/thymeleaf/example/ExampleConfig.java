package edu.thinktank.thymeleaf.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.util.Map;

public class ExampleConfig extends Configuration {
    @JsonProperty("views")
    public Map<String, Map<String, String>> views;
}
