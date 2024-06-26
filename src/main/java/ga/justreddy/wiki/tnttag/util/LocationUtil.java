package ga.justreddy.wiki.tnttag.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    public static String toLocation(Location location) {
        return location.getWorld().getName() + " : " + location.getX() + " : " + location.getY() + " : " + location.getZ() + " : " + location.getYaw() + " : " + location.getPitch();
    }

    public static Location getBlockLocation(String path) {
        String[] split = path.split(" : ");
        return new Location(Bukkit.getWorld(split[0]),
                Math.round(Double.parseDouble(split[1])),
                Math.round(Double.parseDouble(split[2])),
                Math.round(Double.parseDouble(split[3]))
        );
    }

    public static Location getLocation(String path) {
        if (path == null) return null;
        String[] split = path.split(" : ");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static boolean equals(Location loc1, Location loc2) {
        return loc1.getX() == loc2.getX()
                && loc1.getY() == loc2.getY()
                && loc1.getZ() == loc2.getZ();
    }

    public static boolean equalsBlock(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }

}
