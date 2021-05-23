package ${package};

import com.thinkerwolf.gamer.common.util.PropertiesUtil;

/**
 * @author ${author}
 * @since ${date}
 */
public final class ${className} {

<#list props as prop>
    /**
     * ${prop.value}
     */
    public static final String ${prop.name} = PropertiesUtil.getString(LocalMessages.class, "${prop.name}");
</#list>

}