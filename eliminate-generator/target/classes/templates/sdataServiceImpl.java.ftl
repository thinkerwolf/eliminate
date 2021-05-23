package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import com.baomidou.dynamic.datasource.annotation.DS;
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * @author ${author}
 * @since ${date}
 */
@Service
@DS("sdata")
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> {

}
</#if>
