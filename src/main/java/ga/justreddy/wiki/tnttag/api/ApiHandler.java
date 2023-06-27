package ga.justreddy.wiki.tnttag.api;

import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.Game;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ApiHandler implements TntTagAPI {

    @Override
    public CompletableFuture<Optional<TagPlayer>> getTagPlayer(UUID uniqueId) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<TagPlayer>> getTagPlayer(Player player) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Game>> loadTagPlayer(UUID uniqueId) {
        return null;
    }

    @Override
    public Optional<Game> getGame(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Game>> getGames() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Game>> getGamesWithSimilarNames(String nameToCheck) {
        return Optional.empty();
    }
}
