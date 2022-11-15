package app.orm.utils;

import java.util.Map;
import java.util.spi.ToolProvider;

public class TypeConverter {

    private static TypeConverter single_instance = null;
    static Map<String, String> typeMap;

    private TypeConverter() {
        typeMap = File.readJson("src/main/java/app/orm/utils/type conversion.json");
    }

    public static TypeConverter getInstance() {
        if (single_instance == null)
            single_instance = new TypeConverter();

        return single_instance;
    }

    public static String getType(String key) {
        String value = typeMap.get(key);
        return (value == null)? "TEXT(500)" : value;
    }
}