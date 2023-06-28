package ga.justreddy.wiki.tnttag.model.game.timer;

import ga.justreddy.wiki.tnttag.api.game.Game;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
@Getter
public abstract class AbstractTimer implements Runnable {

    private final int seconds;
    protected int ticksExceed = 0;
    private int task;
    private final JavaPlugin plugin;
    private final Game game;


    /**
     * Proper method to use to start the timer is start()
     */

    @Override
    public void run() {
        if (ticksExceed == 0) {
            onEnd();
            stop();
            return;
        }
        --ticksExceed;
        onTick();
    }

    public void start() {
        ticksExceed = seconds;
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20L, 20L);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(task);
    }

    protected abstract void onTick();

    protected abstract void onEnd();

}
