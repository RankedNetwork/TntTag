package ga.justreddy.wiki.tnttag.manager;

import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.model.entity.TagPlayerImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerManager implements Manager {

    Map<UUID, TagPlayer> players;

    public PlayerManager() {
        players = new HashMap<>();
    }

    public TagPlayer addTagPlayer(UUID uuid, String name) {
        TagPlayer tagPlayer = getTagPlayer(uuid);

        if (tagPlayer == null) {
            tagPlayer = new TagPlayerImpl(uuid, name);
            players.put(uuid, tagPlayer);
            return tagPlayer;
        }

        return tagPlayer;
    }

    public TagPlayer getTagPlayer(UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

    @Override
    public void start() {
        // Empty
    }

    @Override
    public void reload() {
        // Empty
    }

    @Override
    public void stop() {
        // Empty
    }
}
