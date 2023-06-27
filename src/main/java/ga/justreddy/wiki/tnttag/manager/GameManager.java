package ga.justreddy.wiki.tnttag.manager;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.model.game.GameImpl;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameManager implements Manager {

    File gamesFolder;

    Map<String, Game> games;

    public GameManager(File file) {
        this.gamesFolder = new File(file.getAbsolutePath() + "/data/games/");
        if (!gamesFolder.exists()) gamesFolder.mkdir();
        this.games = new HashMap<>();
    }

    @Override
    public void start() {
        File[] files = gamesFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            if (games.containsKey(name)) continue;
            register(name, YamlConfiguration.loadConfiguration(file));
        }
        ChatUtil.sendConsole("&bLoaded &l" + games.size() + "&b games");
    }

    @Override
    public void reload() {
        stop();
        start();
    }

    @Override
    public void stop() {
        games.clear();
    }

    public void register(String name, FileConfiguration config) {
        games.putIfAbsent(name, new GameImpl(name, config));
    }

    public File getGamesFolder() {
        return gamesFolder;
    }

    public Map<String, Game> getGames() {
        return games;
    }
}
