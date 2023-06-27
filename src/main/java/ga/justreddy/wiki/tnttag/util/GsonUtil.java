package ga.justreddy.wiki.tnttag.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class GsonUtil {

    public static String toJson(Object object, Class<?> clazz) {
        return (new Gson()).toJson(object, clazz);
    }

    public static <T> T fromJson(String str, Class<T> clazz) {
        return new Gson().fromJson(str, clazz);
    }

    public static <T> T fromJson(JsonElement element, Class<T> clazz) {
        return new Gson().fromJson(element, clazz);
    }

}
