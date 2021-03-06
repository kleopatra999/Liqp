package liqp.tags;

import liqp.Template;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class IncludeTest {

    @Test
    public void renderTest() throws RecognitionException {

        String source =
                "{% assign shape = 'circle' %}\n" +
                "{% include 'color' %}\n" +
                "{% include 'color' with 'red' %}\n" +
                "{% include 'color' with 'blue' %}\n" +
                "{% assign shape = 'square' %}\n" +
                "{% include 'color' with 'red' %}";

        Template template = Template.parse(source);

        String rendered = template.render();

        assertThat(rendered, is("\n" +
                "color: ''\n" +
                "shape: 'circle'\n" +
                "color: 'red'\n" +
                "shape: 'circle'\n" +
                "color: 'blue'\n" +
                "shape: 'circle'\n" +
                "\n" +
                "color: 'red'\n" +
                "shape: 'square'"));
    }
    
    @Test
    public void renderTestWithIncludeDirectorySpecifiedInContext() throws Exception {
        File jekyll = new File(new File("").getAbsolutePath(), "src/test/jekyll");
        File index = new File(jekyll, "index.html");
        File includes = new File(jekyll, "_includes");
        Template template = Template.parse(index);
        Map<String, Object> context = new HashMap<String,Object>();
        context.put(Include.INCLUDES_DIRECTORY_KEY, includes);
        String result = template.render(context);
        assertTrue(result.contains("HEADER"));
    }
}
