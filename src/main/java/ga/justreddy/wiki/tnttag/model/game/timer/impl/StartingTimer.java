package ga.justreddy.wiki.tnttag.model.game.timer.impl;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.model.game.timer.AbstractTimer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StartingTimer extends AbstractTimer {

    public StartingTimer(int seconds, JavaPlugin plugin, Game game) {
        super(seconds, plugin, game);
    }

    @Override
    protected void onTick() {
        int seconds = getTicksExceed();
        FileConfiguration config = TntTag.getCore().getConfigManager()
                .getMessagesConfig().getConfig();
        if (seconds == 20) {
            getGame().sendMessage(config.getString("timers.starting")
                    .replaceAll("<seconds>",
                            (seconds + 1) + " ") +
                            "seconds"
                            );
        }

        if (seconds <= 10) {
            getGame().sendMessage(config.getString("timers.starting")
                    .replaceAll("<seconds>",
                            (seconds + 1) + " ") +
                    ((seconds == 1) ? "second" : "seconds")
            );
        }
    }

    @Override
    protected void onEnd() {

    }
}
