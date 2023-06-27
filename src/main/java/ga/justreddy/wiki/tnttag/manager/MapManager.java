package ga.justreddy.wiki.tnttag.manager;
import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.game.map.GameMap;
import ga.justreddy.wiki.tnttag.model.game.map.GameMapImpl;
import ga.justreddy.wiki.tnttag.util.ChatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapManager implements Manager {

    File worldFolder;

    Map<String, GameMap> gameMaps;

    public MapManager(File file) {
        this.worldFolder = new File(file.getAbsolutePath() + "/data/worlds/");
        if (!worldFolder.exists()) worldFolder.mkdirs();

        this.gameMaps = new HashMap<>();
    }

    @Override
    public void start() {
        File[] files = worldFolder.listFiles();
        if (files == null) return;
        for (File file : files) {
            String name = file.getName();
            if (name.isEmpty() || gameMaps.containsKey(name)) continue;
            register(name);
        }
        ChatUtil.sendConsole("&bLoaded &l" + gameMaps.size() + "&b maps");
    }


    public void register(String name) {;
/*
        gameMaps.put(name, new GameMapImpl(worldFolder, name, true));
*/
    }

    @Override
    public void reload() {
        stop();
        start();
    }

    @Override
    public void stop() {
        gameMaps.clear();
    }

    public GameMap getMapByName(String name) {
        return gameMaps.getOrDefault(name, null);
    }


}
