package edu.thinktank.thymeleaf;

import org.junit.Test;
import org.thymeleaf.templateresource.ClassLoaderTemplateResource;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassResourceTemplateResolverTest {

    @Test
    public void computeTemplateResource() throws Exception {
        final ClassResourceTemplateResolver resolver = new ClassResourceTemplateResolver();

        final String filepath = "/example.html";
        final ITemplateResource resource = resolver.computeTemplateResource(null, null, "/example", filepath, null, null);

        assertThat(resource).isInstanceOf(ClassLoaderTemplateResource.class);
        final ClassLoaderTemplateResource classloaderResource = (ClassLoaderTemplateResource) resource;
        assertThat(classloaderResource.getBaseName()).isEqualTo("example");
        assertThat(classloaderResource.exists()).isTrue();
        assertThat(read(classloaderResource.reader())).isEqualTo(loadFixture(filepath));
    }

    @Test
    public void computeFragmentRelativeToOwnerResource() throws Exception {
        final ClassResourceTemplateResolver resolver = new ClassResourceTemplateResolver();
        final String filepath = "/relative.html";
        final String ownerPath = "/edu/thinktank/thymeleaf";
        final String ownerTemplate = ownerPath + "/example";

        final ITemplateResource resource = resolver.computeTemplateResource(null, ownerTemplate, "/relative", filepath, null, null);

        assertThat(resource).isInstanceOf(ClassLoaderTemplateResource.class);
        final ClassLoaderTemplateResource classloaderResource = (ClassLoaderTemplateResource) resource;
        assertThat(classloaderResource.getBaseName()).isEqualTo("relative");
        assertThat(classloaderResource.exists()).isTrue();
        assertThat(read(classloaderResource.reader())).isEqualTo(loadFixture(ownerPath + filepath));
    }

    public String read(final Reader reader) {
        final char[] arr = new char[8*1024];
        final StringBuffer buf = new StringBuffer();
        int numChars;
        try {
            while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
                buf.append(arr, 0, numChars);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return buf.toString();
    }


    public String loadFixture(final String relativePath) {
        final java.net.URL url = ThymeleafViewRendererTest.class.getResource(relativePath);
        try {
            final java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
            return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        } catch (final IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}