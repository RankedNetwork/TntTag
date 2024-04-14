package ga.justreddy.wiki.tnttag.util;


import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class BukkitUtil {

    public static void calLEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

}
