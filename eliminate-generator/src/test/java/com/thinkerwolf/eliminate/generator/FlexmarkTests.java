package com.thinkerwolf.eliminate.generator;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FlexmarkTests {

    @Test
    public void testBasic() throws Exception {
        String userDir = System.getProperty("user.dir");
        System.out.println("user.dir=" + userDir);

        MutableDataSet options = new MutableDataSet();

        // uncomment to set optional extensions
        //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        File file = new File("../../InterfaceDocs/test.md");
        Reader reader = new InputStreamReader(new FileInputStream(file));
        try {
//            Node document = parser.parse("This is *Sparta*");
            Node document = parser.parseReader(reader);
            String html = renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
            System.out.println(html);


        } catch (Exception e) {

        } finally {
            IOUtils.closeQuietly(reader);
        }

    }


}
