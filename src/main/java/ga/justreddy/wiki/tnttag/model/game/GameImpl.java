package ga.justreddy.wiki.tnttag.model.game;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.Cuboid;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.Round;
import ga.justreddy.wiki.tnttag.api.game.enums.GameState;
import ga.justreddy.wiki.tnttag.model.entity.data.PlayerStatsImpl;
import ga.justreddy.wiki.tnttag.model.game.timer.AbstractTimer;
import ga.justreddy.wiki.tnttag.model.game.timer.impl.StartingTimer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameImpl implements Game {

    final String name;
    final String displayName;
    final FileConfiguration config;
    List<TagPlayer> players;
    List<TagPlayer> its;
    int max;
    int min;
    Cuboid lobbyCuboid;
    Cuboid gameCuboid;
    Location waitingLobby;
    Location spectatorSpawn;
    Location beginSpawn;
    GameState gameState; // TODO
    Round round = null;
    AbstractTimer startingTimer;

    public GameImpl(String name, FileConfiguration config) {
        this.name = name;
        this.config = config;
        this.displayName = config.getString("options.displayName") == null ?
                config.getString("options.displayName") : name;


        startingTimer = new StartingTimer(20, TntTag.getCore().getPlugin(), this);
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
        return config;
    }

    @Override
    public List<TagPlayer> getPlayers() {
        return players;
    }

    @Override
    public List<TagPlayer> getAlivePlayers() {
        return players.stream().filter(player -> !player.isDead()).collect(Collectors.toList());
    }

    @Override
    public List<TagPlayer> getSpectatorPlayers() {
        return players.stream().filter(TagPlayer::isDead).collect(Collectors.toList());
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public Cuboid getLobbyCuboid() {
        return lobbyCuboid;
    }

    @Override
    public Cuboid getGameCuboid() {
        return gameCuboid;
    }

    @Override
    public Location getWaitingLobby() {
        return waitingLobby;
    }

    @Override
    public Location getBeginSpawn() {
        return beginSpawn;
    }

    @Override
    public Optional<Location> getSpectatorSpawn() {
        return Optional.ofNullable(spectatorSpawn);
    }

    @Override
    public int getPlayerCount() {
        return players.size();
    }

    @Override
    public Round getRound() {
        return round;
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
        giveItItems(tagged);
    }

    @Override
    public void sendMessage(String message) {
        players.forEach(player -> player.sendMessage(message));
    }

    @Override
    public void sendTitle(String title, String subTitle) {
        players.forEach(player -> player.sendTitle(title, subTitle));
    }

    @Override
    public void sendActionBar(String message) {
        players.forEach(player -> player.sendActionBar(message));
    }

    @Override
    public void sendSound(String sound) {
        players.forEach(player -> player.sendSound(sound));
    }

    @Override
    public void teleport(Location location) {
        getAlivePlayers().forEach(player -> player.teleport(location));
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
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
        switch (gameState) {
            case WAITING:
                if (getPlayerCount() >= min) {
                    setGameState(GameState.STARTING);
                    startingTimer.start();
                }
                break;
            case STARTING:
                // TODO STARTING
                if (getPlayerCount() < min) {
                    setGameState(GameState.WAITING);
                    // TODO send title
                    startingTimer.stop();
                    return;
                }

                if (startingTimer.getTicksExceed() <= 0) {
                    onGameStart();
                }

                break;
            case PLAYING:
                // TODO PLAYING
                break;
            case ENDING:
                // TODO ENDING
                break;
            case RESTARTING:
                // TODO RESTARTING
        }
    }

    @Override
    public void reset() {

    }

    private void chooseRandomIts() {
        its.clear();
        List<TagPlayer> copy = new ArrayList<>(getAlivePlayers());
        double taggerPercentage = Double.parseDouble("40") / 100.0;
        int numTaggers = (int) Math.round(copy.size() * taggerPercentage);
        Collections.shuffle(copy);

        for (int i = 0; i < numTaggers; i++) {
            TagPlayer tagPlayer = copy.get(i);
            its.add(tagPlayer);
            giveItItems(tagPlayer);
        }

    }

    private void giveItItems(TagPlayer tagPlayer) {
        ItemStack itemStack = new ItemStack(Material.TNT);
        if (!tagPlayer.getPlayer().isPresent()) return;
        Player player = tagPlayer.getPlayer().get();
        PlayerInventory inventory = player.getInventory();
        inventory.setItem(0, itemStack);
        inventory.setHeldItemSlot(0);

    }

    private void startRound() {
        teleport(beginSpawn);
        chooseRandomIts();
        this.round = new RoundImpl(this, 40);
        round.start();
    }

    private void onGameStart() {
        // TODO
        setGameState(GameState.PLAYING);
        startRound();
    }

}
