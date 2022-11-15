package app.utils;

import java.util.HashMap;
import java.util.Map;

public class TypeConverter {

    private static TypeConverter single_instance = null;
    static Map<String, String> typeMap;

    private TypeConverter()
    {
        typeMap = File.readJson("src/main/java/app/utils/type conversion.json");
    }

    public static TypeConverter getInstance()
    {
        if (single_instance == null)
            single_instance = new TypeConverter();

        return single_instance;
    }

    public static String getType(String key){
        return typeMap.get(key);
    }
}
