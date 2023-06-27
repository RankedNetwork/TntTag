package ga.justreddy.wiki.tnttag.listener;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.enums.GameState;
import ga.justreddy.wiki.tnttag.manager.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.UUID;

public class GameListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        TagPlayer tagPlayer = TntTag.getCore().getPlayerManager()
                .getTagPlayer(event.getEntity().getUniqueId());
        if (tagPlayer == null) return;
        if (!tagPlayer.isPlaying()) return;
        Game game = tagPlayer.getGame();
        if (game.getGameState() == GameState.WAITING
                || game.getGameState() == GameState.STARTING) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        TagPlayer entity = TntTag.getCore().getPlayerManager()
                .getTagPlayer(event.getEntity().getUniqueId());
        if (entity == null) return;
        TagPlayer damager = TntTag.getCore().getPlayerManager()
                .getTagPlayer(event.getDamager().getUniqueId());
        if (damager == null) return;
        if (!entity.isPlaying()) return;
        if (!damager.isPlaying()) return;
        if (entity.isDead()) return;
        Game game = entity.getGame();
        if (game.getGameState() != GameState.PLAYING) return;
        if (!game.isIt(damager)) return;
        game.setIt(entity, damager);
        game.sendMessage("&c" + entity.getName() + " &7is IT!");
        ((Player) event.getEntity()).setHealth(20.0);
        ((Player) event.getDamager()).setHealth(20.0);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        TagPlayer tagPlayer = TntTag.getCore().getPlayerManager()
                .getTagPlayer(event.getEntity().getUniqueId());
        if (!tagPlayer.isPlaying()) return;
        event.setFoodLevel(20);
    }

}
