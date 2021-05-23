package com.thinkerwolf.eliminate.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Mybatis-plus模板代码生成
 */
public class SdataCodeGenerator {
    private static final String TO_PROJECT = "/trunk/eliminate-pub";

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + TO_PROJECT;
        globalConfig.setOutputDir(projectPath +"/src/main/java");
        globalConfig.setAuthor("wukai");
        globalConfig.setOpen(false);
        globalConfig.setServiceImplName("%sService");
        mpg.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/eliminate_sdata?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("1234");
        mpg.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.thinkerwolf.eliminate.pub");
        pc.setService("cache");
        pc.setServiceImpl("service");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {

            }

            @Override
            public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
                TableInfo tableInfo = (TableInfo) objectMap.get("table");
                objectMap.put("packageCache",  pc.getParent() + ".cache");
                boolean suc = false;
                for (TableField field : tableInfo.getFields()) {
                    if (field.isKeyFlag()) {
                        if ("boolean".equals(field.getPropertyType())) {
                            objectMap.put("keyGetterName", "is" + field.getCapitalName());
                        } else {
                            objectMap.put("keyGetterName", "get" + field.getCapitalName());
                        }
                        suc = true;
                        break;
                    }
                }
                if (!suc) {
                    objectMap.put("keyGetterName", "getId");
                }
                return objectMap;
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        /*focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });*/

        String cacheTemplatePath = "templates/sdataCache.java.ftl";
        focList.add(new FileOutConfig(cacheTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String p = pc.getParent().replace('.', '/');
                return String.format("%s/src/main/java/%s/cache/%sCache%s", projectPath, p, tableInfo.getEntityName(), StringPool.DOT_JAVA);
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录，自定义目录用");
                if (fileType == FileType.MAPPER) {
                    // 已经生成 mapper 文件判断存在，不想重新生成返回 false
                    return !new File(filePath).exists();
                }
                // 允许生成模板文件
                return true;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setService(null);
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setServiceImpl("templates/sdataServiceImpl.java");
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(false);
        strategy.setControllerMappingHyphenStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));

        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);


        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

    }
}
