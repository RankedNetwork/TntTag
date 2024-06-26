package ga.justreddy.wiki.tnttag.core;

import com.grinderwolf.swm.api.SlimePlugin;
import ga.justreddy.wiki.tnttag.api.game.map.ResetAdapter;
import ga.justreddy.wiki.tnttag.commands.TestCommand;
import ga.justreddy.wiki.tnttag.model.game.map.SlimeMapAdapter;
import ga.justreddy.wiki.tnttag.nms.Nms;
import ga.justreddy.wiki.tnttag.manager.*;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PluginCoreImpl implements PluginCore {

    static String VERSION = getVersion(Bukkit.getServer());

    List<Manager> managers;
    MapManager mapManager;
    GameManager gameManager;
    WorldManager worldManager;
    PlayerManager playerManager;
    ConfigManager configManager;
    JavaPlugin plugin;
    @NonFinal Nms nms;
    @NonFinal ResetAdapter adapter;

    public PluginCoreImpl(JavaPlugin plugin) {
        this.plugin = plugin;
        this.managers = new ArrayList<>();
        addManagers(
                configManager = new ConfigManager(plugin),
                worldManager = new WorldManager(),
                mapManager = new MapManager(plugin.getDataFolder()),
                gameManager = new GameManager(plugin.getDataFolder()),
                playerManager = new PlayerManager()
        );
    }

    @Override
    public void onLoad() {
        // Empty for now
    }

    @Override
    public void onEnable() {
        ChatUtil.sendConsole("&bStarting plugin...");
        ChatUtil.sendConsole("&bTntTag &3v" + plugin.getDescription().getVersion()
                + " &bby &3JustReddy");
        if (!setupNms()) return;
        managers.forEach(Manager::start);

        plugin.getCommand("tnttest").setExecutor(new TestCommand());

        final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        adapter = new SlimeMapAdapter(slimePlugin, slimePlugin.getLoader("file"));

        ChatUtil.sendConsole("&bFinished starting plugin...");
    }

    @Override
    public void onDisable() {
        managers.forEach(Manager::stop);
        managers.clear();
    }

    @Override
    public MapManager getMapManager() {
        return mapManager;
    }

    @Override
    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public WorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public Nms getNms() {
        return nms;
    }

    @Override
    public ResetAdapter getAdapter() {
        return adapter;
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    private boolean setupNms() {
        try {
            nms = (Nms) Class.forName("ga.justreddy.wiki.tnttag.nms." + VERSION + "." + VERSION).newInstance();
            ChatUtil.sendConsole("&bFound &lNMS&b version: &l" + VERSION);
        } catch (Exception ex) {
            ChatUtil.sendConsole("&cFailed to find &lNMS&c version: &l" + VERSION +
                    "&c. It's not supported!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    private void addManagers(Manager... managers) {
        this.managers.addAll(Arrays.asList(managers));
    }

}
