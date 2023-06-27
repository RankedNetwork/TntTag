package ga.justreddy.wiki.tnttag.api;

import ga.justreddy.wiki.tnttag.api.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

public interface Nms {

    boolean isLegacy();

    void sendJson(Player player, String message);

    void sendTitle(Player player, String title, String subTitle);

    void sendActionBar(Player player, String message);

    void sendParticle(Location location, int x, int y, int z, int amount, int data);

    void setIt(Game game, Player it);

    void removeIt(Game game, Player it);

    ChunkGenerator getGenerator();

}
