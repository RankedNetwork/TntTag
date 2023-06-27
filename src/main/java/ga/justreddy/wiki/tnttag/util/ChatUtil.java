package ga.justreddy.wiki.tnttag.util;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.TntTagAPI;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatUtil {

    static String PREFIX = "&7[&dTntTag&7] &r";

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> format(List<String> input) {
        List<String> temp = new ArrayList<>();
        for (String line : input) temp.add(format(line));
        return temp;
    }

    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(format(PREFIX + message));
    }

}
