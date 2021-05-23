package ${packageCache};

import ${package.Entity}.${entity};
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import AbstractCache;
import ${package.ServiceImpl}.${table.serviceImplName};

import java.util.List;

/**
 * @author ${author}
 * @since ${date}
 */
@Component
public class ${entity}Cache extends AbstractCache<Integer, ${entity}> {

    @Autowired
    ${table.serviceImplName} baseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<${entity}> models = baseService.list();
        for (${entity} model : models) {
            put(model.${keyGetterName}(), model);
        }
    }
}