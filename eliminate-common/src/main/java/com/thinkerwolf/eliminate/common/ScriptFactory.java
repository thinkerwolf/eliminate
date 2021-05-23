package com.thinkerwolf.eliminate.common;

import com.google.common.collect.Lists;
import com.thinkerwolf.eliminate.common.annotation.Naming;
import com.thinkerwolf.gamer.common.Constants;
import com.thinkerwolf.gamer.common.util.ClassUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脚本工厂
 * building:1,1
 * copper:1000
 *
 * @author wukai
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ScriptFactory<T> {

    private static final String[] EMPTY_STRINGS = new String[]{};

    private static final Map<Class<?>, ScriptFactory> factoryMap = new ConcurrentHashMap<>();

    private static Set<Class> classSet;

    static {
        classSet = ClassUtils.scanClasses("com.thinkerwolf");
    }

    private static <T> ScriptFactory<T> createFactory(Class<T> baseClass) {
        ScriptFactory<T> factory = new ScriptFactory<>();
        for (Class clazz : classSet) {
            int modifiers = clazz.getModifiers();
            if (baseClass.isAssignableFrom(clazz) && !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                Naming naming = (Naming) clazz.getAnnotation(Naming.class);
                Constructor constructor = clazz.getConstructors()[0];
                if (naming != null) {
                    factory.constructorMap.put(naming.value(), constructor);
                } else {
                    factory.constructorMap.put(clazz.getSimpleName(), constructor);
                }
            }
        }
        return factory;
    }

    public static <T> ScriptFactory<T> getFactory(Class<T> baseClass) {
        return factoryMap.computeIfAbsent(baseClass, aClass -> createFactory(aClass));
    }

    public static <T> List<T> buildList(String scripts, Class<T> baseClass) {
        ScriptFactory factory = getFactory(baseClass);
        List<T> result = Lists.newArrayList();
        for (String script : Constants.SEMICOLON_SPLIT_PATTERN.split(scripts)) {
            if (StringUtils.isBlank(script)) {
                continue;
            }
            try {
                result.add((T) factory.create(script));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    private Class<T> baseClass;

    private final Map<String, Constructor<T>> constructorMap = new HashMap<>();

    public T create(String s) throws Exception {
        s = s.trim();
        String[] ss = Constants.COLON_SPLIT_PATTERN.split(s);
        Constructor<T> constructor = constructorMap.get(ss[0]);
        if (constructor == null) {
            String name = parseName(ss[0]);
            int minOffset = Integer.MAX_VALUE;
            for (Map.Entry<String, Constructor<T>> en : constructorMap.entrySet()) {
                int idx = en.getKey().indexOf(name);
                if (idx > 0) {
                    int offset = en.getKey().length() - (idx + name.length());
                    if (offset == 0) {
                        constructor = en.getValue();
                        break;
                    } else if (offset < minOffset) {
                        constructor = en.getValue();
                        minOffset = offset;
                    }
                }
            }
            if (constructor != null) {
                constructorMap.put(s, constructor);
            }
        }
        if (constructor == null) {
            throw new RuntimeException("Script constructor not found [" + s + "]");
        }

        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] parameterValues = new Object[parameterTypes.length];
        if (parameterTypes.length > 0) {
            String[] paramStrings = ss.length > 1 ? Constants.COMMA_SPLIT_PATTERN.split(ss[1]) : EMPTY_STRINGS;
            for (int i = 0; i < parameterTypes.length; i++) {
                String paramString;
                if (i < paramStrings.length) {
                    paramString = paramStrings[i];
                } else {
                    paramString = parameterTypes[i] == Boolean.TYPE ? "false" : "0";
                }
                parameterValues[i] = ClassUtils.castTo(paramString, parameterTypes[i]);
            }
        }
        return constructor.newInstance(parameterValues);
    }

    private static String parseName(String s) {
        String[] sins = s.split("_");
        StringBuilder sb = new StringBuilder();
        for (String sin : sins) {
            sb.append(Character.toUpperCase(sin.charAt(0))).append(sin.substring(1));
        }
        return sb.toString();

    }

}
