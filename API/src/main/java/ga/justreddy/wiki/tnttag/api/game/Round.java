package ga.justreddy.wiki.tnttag.api.game;

import org.bukkit.entity.Player;

public interface Round {

    Game getGame();

    int getRoundDuration();

    void start();

    void end();

    void updateCompass(Player player);

    Player getNearestSurvivor(Player player);

}
