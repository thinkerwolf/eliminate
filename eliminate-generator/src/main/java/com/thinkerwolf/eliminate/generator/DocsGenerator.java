package com.thinkerwolf.eliminate.generator;

import com.thinkerwolf.gamer.core.mvc.FreemarkerHelper;
import com.vladsch.flexmark.ast.Document;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 将markdown文档转化为html
 *
 * @author wukai
 */
public class DocsGenerator {

    private static final String FROM_DIC = "InterfaceDocs";

    private static final String TO_PROJECT = "trunk/eliminate-game";
    private static final String TO_STATIC_DIC = "src/main/resources/static/docs";

    private static final String INTERFACE_DOC_TEMPLATE = "interface_doc.ftl";
    private static final String INTERFACE_DOC_INDEX_TEMPLATE = "interface_doc_index.ftl";

    public static void main(String[] args) {
        try {
            FreemarkerHelper.init(null);
        } catch (Exception ignored) {
        }
        String userDir = System.getProperty("user.dir");
        System.out.println("user.dir=" + userDir);
        File fromDic = new File(userDir + "/" + FROM_DIC);
        if (!fromDic.exists()) {
            throw new RuntimeException("Docs dic not found " + (userDir + "/" + FROM_DIC));
        }
        System.out.println("markdown path=" + fromDic.getAbsolutePath());

        File toDic = new File(TO_PROJECT + "/" + TO_STATIC_DIC);
        System.out.println("html path=" + toDic.getAbsolutePath());
        try {
            FileUtils.deleteDirectory(toDic);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        MutableDataSet options = new MutableDataSet();
        // uncomment to set optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));

        // uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        List<DocInfo> allDocs = new ArrayList<>();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        generate(fromDic, fromDic.getAbsolutePath(), parser, renderer, allDocs);
        generateIndex(allDocs);
    }

    private static void generate(File dicFile, String parentPath, Parser parser, HtmlRenderer renderer, List<DocInfo> allDocs) {
        if (!dicFile.isDirectory()) {
            return;
        }
        final File[] files = dicFile.listFiles();
        if (files == null) {
            return;
        }
        final String doc = dicFile.getAbsolutePath().substring(parentPath.length());
        String toStaticDic;
        if (StringUtils.isBlank(TO_PROJECT)) {
            toStaticDic = TO_STATIC_DIC + doc;
        } else {
            toStaticDic = TO_PROJECT + "/" + TO_STATIC_DIC + doc;
        }
        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            if (!file.getName().endsWith("md")) {
                continue;
            }
            String module = file.getName().substring(0, file.getName().indexOf('.'));
            try {
                String mdString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                Document document = parser.parse(mdString);
                String body = renderer.render(document);

                Map<String, Object> data = new HashMap<>();
                data.put("serverName", "Game");
                data.put("moduleName", module);
                data.put("body", body);
                byte[] bytes = FreemarkerHelper.getTemplateBytes(INTERFACE_DOC_TEMPLATE, data);

                File toFile = new File(toStaticDic + "/" + module + ".html");
                System.out.println("to -> " + toFile.getAbsolutePath());
                FileUtils.writeByteArrayToFile(toFile, bytes);

                DocInfo docInfo = new DocInfo();
                docInfo.desc = module;
                docInfo.location = StringUtils.isEmpty(doc) ? module + ".html" : doc + "/" + module + ".html";

                allDocs.add(docInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (File file : files) {
            if (file.isDirectory()) {
                generate(file, parentPath, parser, renderer, allDocs);
            }
        }
    }


    private static void generateIndex(List<DocInfo> allDocs) {
        Map<String, Object> data = new HashMap<>(5);
        data.put("serverName", "Game");
        data.put("allDocs", allDocs);
        byte[] bytes = FreemarkerHelper.getTemplateBytes(INTERFACE_DOC_INDEX_TEMPLATE, data);

        String toStaticDic;
        if (StringUtils.isBlank(TO_PROJECT)) {
            toStaticDic = TO_STATIC_DIC;
        } else {
            toStaticDic = TO_PROJECT + "/" + TO_STATIC_DIC;
        }
        File toFile = new File(toStaticDic + "/" + "index.html");
        System.out.println("index to -> " + toFile.getAbsolutePath());
        try {
            FileUtils.writeByteArrayToFile(toFile, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class DocInfo {
        public String desc;
        public String location;

        public String getDesc() {
            return desc;
        }

        public String getLocation() {
            return location;
        }
    }

}
