package ga.justreddy.wiki.tnttag.model.game.map;

import ga.justreddy.wiki.tnttag.api.game.map.GameMap;
import org.bukkit.World;

public class GameMapImpl implements GameMap {
    @Override
    public boolean load() {
        return false;
    }

    @Override
    public void unload() {

    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean restoreFromSource() {
        return false;
    }

    @Override
    public World getWorld() {
        return null;
    }
}
