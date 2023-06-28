package ga.justreddy.wiki.tnttag.config;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class Config {

    /**
     * The name of the key that stores an integer value, which is the current configuration file
     * version.
     */
    private static final String VERSION_KEY = "config-version";

    /**
     * The system file of the config.
     */
    File file;

    /**
     * The actual configuration object which is what you use for modifying and accessing the config.
     */
    FileConfiguration config;

    /**
     * Loads a YAML configuration file. If the file does not exist, a new file will be copied from the
     * project's resources folder.
     *
     * @param plugin Class that extends JavaPlugin
     * @param name The name of the config, ending in .yml
     * @param version The config version
     * @throws InvalidConfigurationException If there was a formatting/parsing error in the config
     * @throws IOException                   If the file could not be created and/or loaded
     */
    public Config(final JavaPlugin plugin, final String name, final int version)
            throws InvalidConfigurationException, IOException {

        final String completeName = name.endsWith(".yml") ? name : name + ".yml";

        final File configFile = new File(plugin.getDataFolder().getAbsolutePath(), completeName);

        if (!configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.getParentFile().mkdirs();
            plugin.saveResource(completeName, false);
        }

        this.file = configFile;
        this.config = YamlConfiguration.loadConfiguration(file);
        reload();
        if (isOutdated(version)) return;
        ChatUtil.sendConsole("&bLoaded config: " + name);
    }

    /**
     * Reloads the configuration file.
     *
     * @throws InvalidConfigurationException If there was a formatting/parsing error in the config
     * @throws IOException                   If the file could not be reloaded
     */
    public void reload() throws InvalidConfigurationException, IOException {
        config.load(file);
    }

    /**
     * Saves the configuration file (after making edits).
     *
     * @throws IOException If the file could not be saved
     */
    public void save() throws IOException {
        config.save(file);
    }
    /**
     * Checks if the config version integer equals or is less than the current version.
     *
     * @param currentVersion The expected value of the config version
     * @return True if the config is outdated, false otherwise
     */
    public boolean isOutdated(final int currentVersion) {
        if (config.getInt(VERSION_KEY, -1) < currentVersion) {
            ChatUtil.sendConsole("&cConfiguration file " + file.getName() + " is outdated! Shutting down plugin.");
            Bukkit.getPluginManager().disablePlugin(TntTag.getCore().getPlugin());
            return true;
        }
        return false;
    }

}
