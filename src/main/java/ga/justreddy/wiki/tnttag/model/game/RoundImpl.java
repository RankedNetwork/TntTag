package ga.justreddy.wiki.tnttag.model.game;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.Round;
import ga.justreddy.wiki.tnttag.manager.PlayerManager;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoundImpl implements Round {

    Game game;
    int roundDuration;

    public RoundImpl(Game game, int roundDuration) {
        this.game = game;
        this.roundDuration = roundDuration;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public int getRoundDuration() {
        return roundDuration;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void updateCompass(Player player) {
        ItemStack compass = player.getInventory().getItem(7);
        Player nearestPlayer = getNearestSurvivor(player);
        if (compass != null && nearestPlayer != null && compass.getItemMeta() != null) {
            ItemMeta meta = compass.getItemMeta();
            meta.setDisplayName((ChatUtil.format("&b" + (int) player.getLocation().distance(nearestPlayer.getLocation()) + "m")));
            compass.setItemMeta(meta);
        }
    }

    @Override
    public Player getNearestSurvivor(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        List<Player> playersInWorld = new ArrayList<>(world.getEntitiesByClass(Player.class));
        if (playersInWorld.size() == 1) {
            return null;
        }

        playersInWorld.remove(player);
        playersInWorld.removeIf(p -> p != null && !game.getAlivePlayers().contains(
                TntTag.getCore().getPlayerManager().getTagPlayer(p.getUniqueId())
        ));
        playersInWorld.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(location)));
        return playersInWorld.isEmpty() ? null : playersInWorld.get(0);
    }
}
