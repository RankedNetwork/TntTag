package ga.justreddy.wiki.tnttag.api.entity;

import ga.justreddy.wiki.tnttag.api.entity.data.PlayerStats;
import ga.justreddy.wiki.tnttag.api.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface TagPlayer {

    UUID getUniqueId();

    String getName();

    Game getGame();

    void setGame(Game game);

    boolean isDead();

    void setDead(boolean dead);

    boolean isPlaying();

    PlayerStats getStats();

    void setStats(PlayerStats stats);

    void sendMessage(String msg);

    void sendTitle(String title, String subTitle);

    void sendActionBar(String msg);

    void sendSound(String sound);

    Optional<Player> getPlayer();

    void teleport(Location location);

}
