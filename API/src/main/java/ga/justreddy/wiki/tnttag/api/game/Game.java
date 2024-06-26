package ga.justreddy.wiki.tnttag.api.game;

import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.enums.GameState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Optional;

public interface Game {

    void init(World world);

    String getName();

    String getDisplayName();

    FileConfiguration getConfig();

    List<TagPlayer> getPlayers();

    List<TagPlayer> getAlivePlayers();

    List<TagPlayer> getSpectatorPlayers();

    int getMax();

    int getMin();

    Cuboid getLobbyCuboid();

    Cuboid getGameCuboid();

    Location getWaitingLobby();

    Location getBeginSpawn();

    Optional<Location> getSpectatorSpawn();

    int getPlayerCount();

    Round getRound();

    GameState getGameState();

    boolean isIt(TagPlayer tagPlayer);

    void setIt(TagPlayer tagged, TagPlayer tagger);

    void sendMessage(String message);

    void sendTitle(String title, String subTitle);

    void sendActionBar(String message);

    void sendSound(String sound);

    void teleport(Location location);

    void setGameState(GameState gameState);

    void onTagPlayerJoin(TagPlayer tagPlayer);

    void onTagPlayerLeave(TagPlayer tagPlayer, boolean silent);

    void onTagPlayerDeath(TagPlayer tagPlayer);

    void startRound();

    void onGameEnd(TagPlayer winner);

    void onCountDown();

    void reset();

}
