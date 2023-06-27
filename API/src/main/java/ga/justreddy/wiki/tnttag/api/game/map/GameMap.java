package ga.justreddy.wiki.tnttag.api.game.map;

import org.bukkit.World;

public interface GameMap {

    boolean load();

    void unload();

    boolean isLoaded();

    boolean restoreFromSource();

    World getWorld();

}
