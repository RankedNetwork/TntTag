package ga.justreddy.wiki.tnttag.manager;
import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.game.map.GameMap;
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

    private final File flatWorldFolder;
    private final File slimeWorldFolder;

    public MapManager() {
        this.flatWorldFolder = new File(
                TntTag.getCore().getPlugin().getDataFolder() + "/data/worlds"
        );
        this.slimeWorldFolder = new File(
                TntTag.getCore().getPlugin().getDataFolder() + "/data/slime"
        );
        if (!flatWorldFolder.exists()) flatWorldFolder.mkdirs();
        if (!slimeWorldFolder.exists()) slimeWorldFolder.mkdirs();

    }


    @Override
    public void start() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void stop() {

    }
}
