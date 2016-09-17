package edu.thinktank.thymeleaf;

import edu.thinktank.thymeleaf.views.AbsoluteView;
import io.dropwizard.views.View;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanContextTest {

    @Test
    public void loadBeanStyleGetterMethods() throws Exception {
        final View view = new AbsoluteView("test");

        final BeanContext beanContext = new BeanContext(view, Locale.GERMAN);

        assertThat(beanContext.getVariableNames()).containsExactlyInAnyOrder("name", "message");
    }

}