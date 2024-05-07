package ga.justreddy.wiki.tnttag.api.game.map;

import ga.justreddy.wiki.tnttag.api.game.Game;
import org.bukkit.World;

public interface ResetAdapter {

    void onEnable(Game game);

    void onRestart(Game game);

    void onDisable(Game game);

    boolean worldExists(String name);

}
