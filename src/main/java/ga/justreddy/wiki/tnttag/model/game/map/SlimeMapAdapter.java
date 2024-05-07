package ga.justreddy.wiki.tnttag.model.game.map;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.map.ResetAdapter;
import ga.justreddy.wiki.tnttag.manager.MapManager;
import ga.justreddy.wiki.tnttag.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.DuplicateFormatFlagsException;

/**
 * @author JustReddy
 */
public class SlimeMapAdapter implements ResetAdapter {

    private static final JavaPlugin PLUGIN = TntTag.getCore().getPlugin();

    private final SlimePlugin slime;
    private final SlimeLoader loader;

    public SlimeMapAdapter(SlimePlugin slime, SlimeLoader loader) {
        this.slime = slime;
        this.loader = loader;
    }

    @Override
    public void onEnable(Game game) {
        Bukkit.getScheduler().runTask(PLUGIN, () -> {

            File originalWorldFile = new File("slime_worlds/" + game.getName() + ".slime");

            File newWorldFile = new File(TntTag.getCore().getMapManager().getSlimeWorldFolder().getAbsolutePath() + "/" + game.getName() + ".slime");

            try {
                FileUtil.copy(originalWorldFile, newWorldFile);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to copy world file", ex);
            }

            World world = Bukkit.getWorld(game.getName());

            if (world != null) {
                game.init(world);
                return;
            }

            Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
                try {
                    SlimeWorld slimeWorld = slime.loadWorld(loader,
                            game.getName(), true, getPropertyMap());

                    Bukkit.getScheduler().runTask(PLUGIN, () -> {
                        slime.generateWorld(slimeWorld);
                        World generatedWorld = Bukkit.getWorld(game.getName());
                        if (generatedWorld == null) {
                            onDisable(game);
                        }
                        game.init(generatedWorld);
                    });
                } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                         WorldInUseException ex) {
                    onDisable(game);
                    throw new RuntimeException("Failed to load world", ex);
                }
            });
        });
    }

    @Override
    public void onRestart(Game game) {
        Bukkit.getScheduler().runTask(PLUGIN, () -> {
            Bukkit.unloadWorld(game.getName(), false);
            Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
                File originalWorldFolder = new File(
                        "slime_worlds/" + game.getName() + ".slime"
                );
                FileUtil.delete(originalWorldFolder);
                try {
                    if (loader.worldExists(game.getName())) {
                        loader.deleteWorld(game.getName());
                    }
                }catch (UnknownWorldException | IOException ex) {
                    throw new RuntimeException("Failed to delete world", ex);
                }
                onEnable(game);
            });
        });
    }

    @Override
    public void onDisable(Game game) {
        Bukkit.getScheduler().runTask(PLUGIN, () -> {
            Bukkit.unloadWorld(game.getName(), false);
            Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
                File originalWorldFolder = new File(
                        "slime_worlds/" + game.getName() + ".slime"
                );
                FileUtil.delete(originalWorldFolder);
                try {
                    if (loader.worldExists(game.getName())) {
                        loader.deleteWorld(game.getName());
                    }
                }catch (UnknownWorldException | IOException e) {
                    throw new RuntimeException("Failed to delete world", e);
                }
            });
        });
    }

    @Override
    public boolean worldExists(String name) {
        try {
            return loader.worldExists(name);
        } catch (IOException e) {
            throw new RuntimeException("Failed to check if world exists", e);
        }
    }


    private SlimePropertyMap getPropertyMap() {
        SlimePropertyMap map = new SlimePropertyMap();
        map.setInt(SlimeProperties.SPAWN_X, 0);
        map.setInt(SlimeProperties.SPAWN_Y, 30);
        map.setInt(SlimeProperties.SPAWN_Z, 0);
        map.setBoolean(SlimeProperties.PVP, true);
        map.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        map.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        map.setString(SlimeProperties.DIFFICULTY, "easy");
        return map;
    }
}
