package com.thinkerwolf.eliminate.generator;

import com.thinkerwolf.gamer.common.util.PropertiesUtil;
import com.thinkerwolf.gamer.core.mvc.FreemarkerHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.File;
import java.util.*;

public class LocalMessagesGenerator {
    private static final String TO_PATH = "trunk/eliminate-common/src/main/java";
    private static final String PACKAGE = "com.thinkerwolf.eliminate.common";
    private static final String CLASS_NAME = "LocalMessages";
    private static final String TEMPLATE = "localMessages.java.ftl";

    public static void main(String[] args) throws Exception {
        try {
            FreemarkerHelper.init(null);
        } catch (Exception ignored) {
        }
        String userDir = System.getProperty("user.dir");
        System.out.println("user.dir=" + userDir);

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("package", PACKAGE);
        templateData.put("author", "wukai");
        templateData.put("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        templateData.put("className", CLASS_NAME);

        ResourceBundle bundle = PropertiesUtil.getBundle(LocalMessagesGenerator.class, Locale.CHINA);
        List<Map<String, String>> props = new ArrayList<>();
        List<String> keys = new ArrayList<>(bundle.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            Map<String, String> prop = new HashMap<>();
            prop.put("name", key);
            prop.put("value", bundle.getString(key));
            props.add(prop);
            System.out.println(prop);
        }
        templateData.put("props", props);
        File toFile = new File(userDir + "/" + TO_PATH + "/" + PACKAGE.replace('.', '/') + "/" + CLASS_NAME + ".java");
        byte[] data = FreemarkerHelper.getTemplateBytes(TEMPLATE, templateData);
        FileUtils.writeByteArrayToFile(toFile, data);
        System.out.println("to -> " + toFile.getAbsolutePath());
    }

}
