package ga.justreddy.wiki.tnttag.model.game;

import ga.justreddy.wiki.tnttag.TntTag;
import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.events.GameStartEvent;
import ga.justreddy.wiki.tnttag.api.events.PlayerGameJoinEvent;
import ga.justreddy.wiki.tnttag.api.events.PlayerGameLeaveEvent;
import ga.justreddy.wiki.tnttag.api.game.Cuboid;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.api.game.Round;
import ga.justreddy.wiki.tnttag.api.game.enums.GameState;
import ga.justreddy.wiki.tnttag.model.entity.data.PlayerStatsImpl;
import ga.justreddy.wiki.tnttag.model.game.timer.AbstractTimer;
import ga.justreddy.wiki.tnttag.model.game.timer.impl.StartingTimer;
import ga.justreddy.wiki.tnttag.util.BukkitUtil;
import ga.justreddy.wiki.tnttag.util.LocationUtil;
import ga.justreddy.wiki.tnttag.util.player.PlayerUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    List<TagPlayer> spectators;
    int max;
    int min;
    int roundNumber;
    Cuboid lobbyCuboid;
    Cuboid gameCuboid;
    Location waitingLobby;
    Location spectatorSpawn;
    Location beginSpawn;
    GameState gameState; // TODO
    Round round = null;
    AbstractTimer startingTimer;
    AbstractTimer endTimer;
    World world;

    public GameImpl(String name, FileConfiguration config) {
        this.name = name;
        this.config = config;
        this.displayName = config.getString("options.displayName") == null ?
                config.getString("options.displayName") : name;
        this.startingTimer = new StartingTimer(20, TntTag.getCore().getPlugin(), this);
        this.endTimer = new StartingTimer(10, TntTag.getCore().getPlugin(), this);
        this.players = new ArrayList<>();
        this.its = new ArrayList<>();
        this.spectators = new ArrayList<>();
    }

    @Override
    public void init(World world) {
        this.world = world;
        players.clear();
        its.clear();
        spectators.clear();
        max = config.getInt("options.max");
        min = config.getInt("options.min");
        roundNumber = 0;

        this.waitingLobby = LocationUtil.getLocation(config.getString("locations.waitingLobby.spawn"));
        if (waitingLobby != null) {
            Location high = LocationUtil.getLocation(config.getString("locations.waitingLobby.high"));
            Location low = LocationUtil.getLocation(config.getString("locations.waitingLobby.low"));
            lobbyCuboid = new Cuboid(high, low);
        }

        Location high = LocationUtil.getLocation(config.getString("locations.game.high"));
        Location low = LocationUtil.getLocation(config.getString("locations.game.low"));
        gameCuboid = new Cuboid(high, low);
        this.startingTimer = new StartingTimer(20, TntTag.getCore().getPlugin(), this);
        this.beginSpawn = LocationUtil.getLocation(config.getString("locations.beginSpawn"));
        this.spectatorSpawn = LocationUtil.getLocation(config.getString("locations.spectatorSpawn"));
        setGameState(GameState.WAITING);
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
        if (taggedPlayer == null) return;
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


        Player bukkitPlayer = tagPlayer.getPlayer().orElse(null);

        if (bukkitPlayer == null) return;

        bukkitPlayer.setAllowFlight(false);
        bukkitPlayer.setFlying(false);

        players.add(tagPlayer);
        tagPlayer.setGame(this);
        bukkitPlayer.getInventory().setHeldItemSlot(4);
        bukkitPlayer.setGameMode(GameMode.ADVENTURE);

        if (waitingLobby != null && lobbyCuboid != null) {
            tagPlayer.teleport(waitingLobby);
        } else {
            tagPlayer.teleport(beginSpawn);
        }

        PlayerUtil.refresh(bukkitPlayer);

        // TODO possibly set their rank ?
        Bukkit.getScheduler().runTaskLater(TntTag.getCore().getPlugin(), () -> {
            // TODO set scoreboard

            PlayerUtil.clearInventory(bukkitPlayer);

            for (Player player1 : world.getPlayers()) {
                tagPlayer.getPlayer().ifPresent(player -> {
                    player1.showPlayer(player);
                    player.showPlayer(player1);
                });
            }

            // TODO add items maybe?
        }, 5L);

        PlayerGameJoinEvent event = new PlayerGameJoinEvent(tagPlayer);
        event.call();

    }

    @Override
    public void onTagPlayerLeave(TagPlayer tagPlayer, boolean silent) {
        spectators.remove(tagPlayer);
        players.remove(tagPlayer);
        tagPlayer.setGame(null);
        tagPlayer.setDead(false);

        // TODO remove scoreboard

        // TODO properly remove player from game
        tagPlayer.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());

        PlayerGameLeaveEvent event = new PlayerGameLeaveEvent(this, tagPlayer);
        event.call();

        if (silent) return;
        // tODo send message that left

    }

    @Override
    public void onTagPlayerDeath(TagPlayer tagPlayer) {
        if (tagPlayer.isDead()) return;

        /**
         * No need to fire an event here
         * Because {@Class PlayerLostRoundEvent} will be fired in {@Class RoundImpl#end()}
         */

        tagPlayer.setDead(true);
        Player bukkitPlayer = tagPlayer.getPlayer().orElse(null);
        if (bukkitPlayer == null) return;
        bukkitPlayer.setGameMode(GameMode.ADVENTURE);

        PlayerUtil.refresh(bukkitPlayer);
        spectators.add(tagPlayer);
        Bukkit.getScheduler().runTaskLater(TntTag.getCore().getPlugin(), () -> {
            bukkitPlayer.teleport(spectatorSpawn);
            bukkitPlayer.setAllowFlight(true);
            bukkitPlayer.setFlying(true);
            bukkitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 4));
        }, 3L);

        for (TagPlayer player : players) {
            player.getPlayer().ifPresent(player1 -> {
                player1.hidePlayer(bukkitPlayer);
            });
        }

        for (TagPlayer player : getSpectatorPlayers()) {
            player.getPlayer().ifPresent(player1 -> {
                player1.hidePlayer(bukkitPlayer);
            });
        }

        // TODO send title maybe ?

    }

    @Override
    public void startRound() {
        teleport(beginSpawn);
        chooseRandomIts();
        roundNumber += 1;
        this.round = new RoundImpl(this, 40, roundNumber);
        round.start();
    }

    @Override
    public void onGameEnd(TagPlayer winner) {
        setGameState(GameState.ENDING);
    }

    @Override
    public void onCountDown() {
        if (world == null) return;
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
                // DONT NEED ANYTHING HERE AT THE MOMENT
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
        setGameState(GameState.RESTARTING);
        world.getPlayers().forEach(player -> {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        });
        players.clear();
        its.clear();
        roundNumber = 0;
        round = null;
        startingTimer.stop();
        Bukkit.getScheduler().runTaskLater(TntTag.getCore().getPlugin(), () -> {
            TntTag.getCore().getAdapter().onRestart(this);
        }, 20L);
    }

    private void chooseRandomIts() {
        its.clear();
        List<TagPlayer> copy = new ArrayList<>(getAlivePlayers());
        double taggerPercentage = 40 / 100.0;
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

    private void onGameStart() {
        setGameState(GameState.PLAYING);
        BukkitUtil.calLEvent(new GameStartEvent(this));
        startRound();
    }

}
