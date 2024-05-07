package ga.justreddy.wiki.tnttag.util.player;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * @author JustReddy
 */
public class PlayerUtil {

    public static void refresh(Player player) {
        if (player == null) return;
        TagPlayer tagPlayer = TntTag.getCore().getPlayerManager().getTagPlayer(player.getUniqueId());
        if (tagPlayer == null) return;
        player.setMaxHealth(20.0D);
        player.setHealth(20.0D);
        player.setHealthScale(20.0D);
        player.setNoDamageTicks(0);
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setExp(0.0F);
        player.setLevel(0);
        Bukkit.getScheduler().runTaskLater(TntTag.getCore().getPlugin(), () -> {
            player.setAllowFlight(false);
            player.setFlying(false);
        }, 3L);
        player.setFallDistance(0.0F);
        removeEffects(player);
    }

    public static void setPlayerVelocity(Player player, Vector velocity) {
        player.setVelocity(velocity);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.getVelocity().add(new Vector(0, -2.2F, 0));
            }
        }.runTaskLater(TntTag.getCore().getPlugin(), 20L);
    }

    public static boolean isPlayerWithinRadius(Player player, Location center, double radius) {
        Location playerLocation = player.getLocation();
        double distanceSquared = center.distanceSquared(playerLocation);
        return distanceSquared <= radius * radius;
    }

    public static void removeEffects(Player player) {
        player.getActivePotionEffects()
                .forEach(effect -> player.removePotionEffect(effect.getType()));
    }

    public static void clearInventory(Player player) {
        if (player == null) return;
        player.closeInventory();
        PlayerInventory inventory = player.getInventory();
        inventory.setArmorContents(null);
        inventory.clear();
    }

    public static boolean hasPermissions(Player player, String... permissions) {
        for (val perm : permissions) {
            if (player.hasPermission(perm)) return true;
        }
        return false;
    }

}
