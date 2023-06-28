package ga.justreddy.wiki.tnttag.core;

import ga.justreddy.wiki.tnttag.nms.Nms;
import ga.justreddy.wiki.tnttag.manager.*;
import org.bukkit.plugin.java.JavaPlugin;

public interface PluginCore {

    void onLoad();

    void onEnable();

    void onDisable();

    MapManager getMapManager();

    GameManager getGameManager();

    WorldManager getWorldManager();

    PlayerManager getPlayerManager();

    ConfigManager getConfigManager();

    Nms getNms();

    JavaPlugin getPlugin();

}
