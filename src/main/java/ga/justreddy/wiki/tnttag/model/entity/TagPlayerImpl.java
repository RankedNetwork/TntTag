package ga.justreddy.wiki.tnttag.model.entity;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.entity.data.PlayerStats;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.model.entity.data.PlayerStatsImpl;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagPlayerImpl implements TagPlayer {

    UUID uniqueId;
    String name;
    Player player;
    @NonFinal Game game = null;
    @NonFinal boolean dead = false;
    @NonFinal PlayerStats stats;

    public TagPlayerImpl(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.player = Bukkit.getPlayer(uniqueId);
        this.stats = new PlayerStatsImpl();
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public boolean isPlaying() {
        return game != null;
    }

    @Override
    public PlayerStats getStats() {
        return stats;
    }

    @Override
    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }

    @Override
    public void sendMessage(String msg) {
        if (msg.startsWith("json:")) {
            // TODO
            return;
        }
        player.sendMessage(ChatUtil.format(msg));
    }

    @Override
    public void sendTitle(String title, String subTitle) {
        TntTag.getCore().getNms().sendTitle(player, ChatUtil.format(title), ChatUtil.format(subTitle));
    }

    @Override
    public void sendActionBar(String msg) {

    }

    @Override
    public void sendSound(String sound) {

    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }
}
