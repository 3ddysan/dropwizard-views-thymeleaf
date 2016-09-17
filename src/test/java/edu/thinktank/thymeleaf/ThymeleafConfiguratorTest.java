package edu.thinktank.thymeleaf;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.standard.StandardDialect;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ThymeleafConfiguratorTest {

    @Test
    public void createDefaultConfiguration() throws Exception {
        final ThymeleafConfigurator configurator = new ThymeleafConfigurator(new HashMap<>());

        assertThat(configurator.getPrefix()).isEmpty();
        assertThat(configurator.getSuffix()).isEqualTo(".html");
        assertThat(configurator.getTemplateMode()).isEqualTo("HTML");
        assertThat(configurator.isCacheable()).isTrue();
    }

    @Test
    public void createAndApplyDefaultConfiguration() throws Exception {
        final ThymeleafConfigurator configurator = new ThymeleafConfigurator(new HashMap<>());
        final ClassResourceTemplateResolver resolver = new ClassResourceTemplateResolver();
        final TemplateEngine engine = new TemplateEngine();
        final Set<IDialect> dialects = new LinkedHashSet<>();
        dialects.add(new StandardDialect());
        dialects.add(new Java8TimeDialect());

        configurator.apply(resolver, engine);

        assertThat(resolver.getPrefix()).isNull();
        assertThat(resolver.getSuffix()).isEqualTo(configurator.getSuffix());
        assertThat(resolver.getTemplateMode().name()).isEqualTo(configurator.getTemplateMode());
        assertThat(resolver.isCacheable()).isEqualTo(configurator.isCacheable());

        assertThat(engine.getTemplateResolvers()).contains(resolver);
        assertThat(engine.getDialects()).hasAtLeastOneElementOfType(StandardDialect.class);
        assertThat(engine.getDialects()).hasAtLeastOneElementOfType(Java8TimeDialect.class);
    }

    @Test
    public void createAndApplyCustomConfiguration() throws Exception {
        final String isCachableString = "false";
        final String prefix = "/templates/static/";
        final String css = "css";
        final String cssSuffix = "." + css;

        final HashMap<String, String> options = new HashMap<>();
        options.put(ThymeleafConfigurator.THYMELEAF_CACHE_KEY, isCachableString);
        options.put(ThymeleafConfigurator.THYMELEAF_PREFIX_KEY, prefix);
        options.put(ThymeleafConfigurator.THYMELEAF_SUFFIX_KEY, cssSuffix);
        options.put(ThymeleafConfigurator.THYMELEAF_TEMPLATEMODE_KEY, css);

        final ThymeleafConfigurator configurator = new ThymeleafConfigurator(options);
        final ClassResourceTemplateResolver resolver = new ClassResourceTemplateResolver();
        final TemplateEngine engine = new TemplateEngine();

        final Set<IDialect> dialects = new LinkedHashSet<>();
        dialects.add(new StandardDialect());
        dialects.add(new Java8TimeDialect());

        configurator.apply(resolver, engine);

        assertThat(resolver.getPrefix()).isNull(); // setting prefix on resolver is always disabled
        assertThat(resolver.getSuffix()).isEqualTo(cssSuffix);
        assertThat(resolver.getTemplateMode().name()).isEqualTo(css.toUpperCase());
        assertThat(resolver.isCacheable()).isEqualTo(Boolean.valueOf(isCachableString));
    }

}
