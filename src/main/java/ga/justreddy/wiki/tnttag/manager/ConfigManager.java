package ga.justreddy.wiki.tnttag.manager;

import ga.justreddy.wiki.tnttag.config.Config;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class ConfigManager implements Manager {

    final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    Config messagesConfig;

    @Override
    public void start() {
        String current = "Config File";
        try {
            current = "messages.yml";
            messagesConfig = new Config(plugin, current, 1);
        }catch (InvalidConfigurationException | IOException exception) {
            ChatUtil.sendConsole("&cError while loading config file: " + current);
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public void stop() {

    }



}
