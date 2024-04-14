package ga.justreddy.wiki.tnttag.commands;

import ga.justreddy.wiki.tnttag.TntTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author JustReddy
 */
public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        for (Player p : player.getWorld().getPlayers()) {
            if (p.getUniqueId().equals(player.getUniqueId())) continue;
            TntTag.getCore().getNms()
                    .removeIt(null, p);
        }

        TntTag.getCore().getNms().setIt(null, player);

        return true;
    }
}
