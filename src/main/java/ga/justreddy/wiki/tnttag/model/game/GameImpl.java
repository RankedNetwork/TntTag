package ga.justreddy.wiki.tnttag.model.game;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.Cuboid;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.enums.GameState;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Optional;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameImpl implements Game {

    String name;
    String displayName;
    FileConfiguration config;
    List<TagPlayer> players;
    List<TagPlayer> its;
    int max;
    int min;
    int round;
    Cuboid lobbyCuboid;
    Cuboid gameCuboid;
    Location waitingLobby;
    Location spectatorSpawn;
    GameState gameState; // TODO

    public GameImpl(String name, FileConfiguration config) {
        this.name = name;
        this.config = config;
        this.displayName = config.getString("options.displayName") == null ?
                config.getString("options.displayName") : name;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public FileConfiguration getConfig() {
        return null;
    }

    @Override
    public List<TagPlayer> getPlayers() {
        return null;
    }

    @Override
    public List<TagPlayer> getAlivePlayers() {
        return null;
    }

    @Override
    public List<TagPlayer> getSpectatorPlayers() {
        return null;
    }

    @Override
    public int getMax() {
        return 0;
    }

    @Override
    public int getMin() {
        return 0;
    }

    @Override
    public Cuboid getLobbyCuboid() {
        return null;
    }

    @Override
    public Cuboid getGameCuboid() {
        return null;
    }

    @Override
    public Location getWaitingLobby() {
        return null;
    }

    @Override
    public Optional<Location> getSpectatorSpawn() {
        return Optional.ofNullable(spectatorSpawn);
    }

    @Override
    public int getPlayerCount() {
        return 0;
    }

    @Override
    public int getRound() {
        return 0;
    }

    @Override
    public void setRound(int round) {

    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public boolean isIt(TagPlayer tagPlayer) {
        return its.contains(tagPlayer);
    }

    @Override
    public void setIt(TagPlayer tagged, TagPlayer tagger) {
        its.remove(tagger);
        its.add(tagged);
        Player taggedPlayer = tagged.getPlayer().orElse(null);
        if (taggedPlayer ==  null) return;
        Player taggerPlayer = tagger.getPlayer().orElse(null);
        if (taggerPlayer == null) return;
        TntTag.getCore().getNms().removeIt(this, taggerPlayer);
        TntTag.getCore().getNms().setIt(this, taggedPlayer);
        PlayerInventory taggedInventory = taggedPlayer.getInventory();
        PlayerInventory taggerInventory = taggerPlayer.getInventory();
        ItemStack itemStack = new ItemStack(Material.TNT);
        if (taggerInventory.getHelmet() != null) {
            taggerInventory.setHelmet(null);
        }
        taggedInventory.setHelmet(itemStack);
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendTitle(String title, String subTitle) {

    }

    @Override
    public void sendActionBar(String message) {

    }

    @Override
    public void sendSound(String sound) {

    }

    @Override
    public void setGameState(GameState gameState) {

    }

    @Override
    public void onTagPlayerJoin(TagPlayer tagPlayer) {

    }

    @Override
    public void onTagPlayerLeave(TagPlayer tagPlayer, boolean silent) {

    }

    @Override
    public void onTagPlayerDeath(TagPlayer tagPlayer) {

    }

    @Override
    public void onCountDown() {

    }

    @Override
    public void reset() {

    }
}
