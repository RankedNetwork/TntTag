package ga.justreddy.wiki.tnttag;

import ga.justreddy.wiki.tnttag.api.Nms;
import ga.justreddy.wiki.tnttag.core.PluginCore;
import ga.justreddy.wiki.tnttag.core.PluginCoreImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class TntTag extends JavaPlugin {

    @Getter static PluginCore core;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        // So clean :smile:
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        core = new PluginCoreImpl(this);
        core.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        core.onDisable();
    }
}
