package ga.justreddy.wiki.tnttag.api;

import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.Game;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TntTagAPI {

    CompletableFuture<Optional<TagPlayer>> getTagPlayer(UUID uniqueId);

    CompletableFuture<Optional<TagPlayer>> getTagPlayer(Player player);

    CompletableFuture<Optional<Game>> loadTagPlayer(UUID uniqueId);

    Optional<Game> getGame(String name);

    Optional<List<Game>> getGames();

    Optional<List<Game>> getGamesWithSimilarNames(String nameToCheck);

}
