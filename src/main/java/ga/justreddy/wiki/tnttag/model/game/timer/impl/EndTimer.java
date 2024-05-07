package ga.justreddy.wiki.tnttag.model.game.timer.impl;

import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.enums.GameState;
import ga.justreddy.wiki.tnttag.model.game.timer.AbstractTimer;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author JustReddy
 */
public class EndTimer extends AbstractTimer {
    public EndTimer(int seconds, JavaPlugin plugin, Game game) {
        super(seconds, plugin, game);
    }

    @Override
    protected void onTick() {
        if (getTicksExceed() == 0) getGame().reset();
    }

    @Override
    protected void onEnd() {}
}
