package ga.justreddy.wiki.tnttag.core;

import ga.justreddy.wiki.tnttag.api.Nms;
import ga.justreddy.wiki.tnttag.manager.GameManager;
import ga.justreddy.wiki.tnttag.manager.MapManager;
import ga.justreddy.wiki.tnttag.manager.PlayerManager;
import ga.justreddy.wiki.tnttag.manager.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public interface PluginCore {

    void onLoad();

    void onEnable();

    void onDisable();

    MapManager getMapManager();

    GameManager getGameManager();

    WorldManager getWorldManager();

    PlayerManager getPlayerManager();

    Nms getNms();

    JavaPlugin getPlugin();

}
