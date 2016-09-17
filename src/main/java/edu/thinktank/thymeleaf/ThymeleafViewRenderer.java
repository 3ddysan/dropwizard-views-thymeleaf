package edu.thinktank.thymeleaf;

import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

public class ThymeleafViewRenderer implements ViewRenderer {

    private final ClassResourceTemplateResolver templateResolver;
    private final TemplateEngine templateEngine;
    private ThymeleafConfigurator config;

    public ThymeleafViewRenderer() {
        this(new ClassResourceTemplateResolver(), new TemplateEngine());
    }

    public ThymeleafViewRenderer(final ClassResourceTemplateResolver templateResolver, final TemplateEngine templateEngine) {
        this.templateResolver = templateResolver;
        this.templateEngine = templateEngine;
    }

    public boolean isRenderable(final View view) {
        return view.getTemplateName().endsWith(this.config.getSuffix());
    }

    public void render(final View view, final Locale locale, final OutputStream output) throws IOException {
        final String templateNameWithPath = this.config.getPrefix() + view.getTemplateName().replace(this.config.getSuffix(), "");
        final OutputStreamWriter writer = new OutputStreamWriter(output, view.getCharset().orElse(StandardCharsets.UTF_8));
        templateEngine.process(templateNameWithPath, new BeanContext(view, locale), writer);
        writer.flush();
    }

    public void configure(final Map<String, String> options) {
        config = new ThymeleafConfigurator(options);
        config.apply(this.templateResolver, this.templateEngine);
    }

    public String getSuffix() {
        return ".html";
    }
}
