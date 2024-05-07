package ga.justreddy.wiki.tnttag.model.game;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.events.PlayerLostRoundEvent;
import ga.justreddy.wiki.tnttag.api.events.PlayerWonRoundEvent;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.Round;
import ga.justreddy.wiki.tnttag.manager.PlayerManager;
import ga.justreddy.wiki.tnttag.util.BukkitUtil;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoundImpl implements Round {

    Game game;
    @NonFinal int roundDuration;
    int roundNumber;
    FileConfiguration config = TntTag.getCore().getConfigManager()
            .getMessagesConfig().getConfig();

    public RoundImpl(Game game, int roundDuration, int roundNumber) {
        this.game = game;
        this.roundDuration = roundDuration;
        this.roundNumber = roundNumber;
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
    public int getRoundNumber() {
        return roundNumber;
    }

    @Override
    public void start() {
        game.sendTitle(config.getString("titles.round-start.title"), config.getString("titles.round-start.subtitle"));
        new BukkitRunnable() {
            @Override
            public void run() {
                --roundDuration;
                game.getAlivePlayers().forEach(player -> {
                    Player bukkitPlayer = player.getPlayer().orElse(null);
                    if (bukkitPlayer == null) return;
                    bukkitPlayer.setLevel(Math.max(roundDuration, 0));

                    if (game.isIt(player)) {
                        updateCompass(bukkitPlayer);
                        player.sendActionBar(config.getString("actionbars.it"));
                    } else {
                        player.sendActionBar(config.getString("actionbars.survivor"));
                    }

                    if (roundDuration == 0) {
                        cancel();
                        end();

                        if (game.getAlivePlayers().size() == 1) {
                            game.onGameEnd(game.getAlivePlayers().get(0));
                        } else {
                            game.startRound();
                        }

                    } else if (roundDuration < 0) {
                        cancel();
                        end();
                        ChatUtil.sendConsole("&cGame " + game.getName() + " crashed... stopping game");
                        game.reset();
                    }

                });

            }
        }.runTaskTimer(TntTag.getCore().getPlugin(), 20L, 20L);

    }

    @Override
    public void end() {
        game.sendMessage(config.getString("game.round-end"));
        game.getAlivePlayers().forEach(player -> {
            Player bukkitPlayer = player.getPlayer().orElse(null);
            if (bukkitPlayer == null) return;
            if (player.isDead()) return;
            if (game.isIt(player)) {
                BukkitUtil.calLEvent(new PlayerLostRoundEvent(game, player));
                game.onTagPlayerDeath(player);
            } else {
                BukkitUtil.calLEvent(new PlayerWonRoundEvent(game, player));
                // TODO play fireworks or something
            }
        });

    }

    @Override
    public void updateCompass(Player player) {
        ItemStack compass = player.getInventory().getItem(7);
        Player nearestPlayer = getNearestSurvivor(player);
        if (compass != null && nearestPlayer != null && compass.getItemMeta() != null) {
            ItemMeta meta = compass.getItemMeta();
            meta.setDisplayName((ChatUtil.format("&b" + (int) player.getLocation().distance(nearestPlayer.getLocation()) + "m")));
            player.setCompassTarget(nearestPlayer.getLocation());
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
        /*playersInWorld.removeIf(p -> p != null && !game.getAlivePlayers().contains(
                TntTag.getCore().getPlayerManager().getTagPlayer(p.getUniqueId())
        ));*/
        playersInWorld.removeIf(Objects::isNull);
        playersInWorld.removeIf(p -> !game.getAlivePlayers().contains(
                TntTag.getCore().getPlayerManager().getTagPlayer(p.getUniqueId())
        ));
        playersInWorld.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(location)));
        return playersInWorld.isEmpty() ? null : playersInWorld.get(0);
    }
}
