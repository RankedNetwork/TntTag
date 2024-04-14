package ga.justreddy.wiki.tnttag.nms.v1_8_R3;

import ga.justreddy.wiki.tnttag.api.entity.TagPlayer;
import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.nms.Nms;
import ga.justreddy.wiki.tnttag.team.FakeTeam;
import ga.justreddy.wiki.tnttag.team.FakeTeamManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.*;

public final class v1_8_R3 implements Nms {

    @Override
    public boolean isLegacy() {
        return true;
    }

    @Override
    public void sendJson(Player player, String message) {
        if (!message.startsWith("json:")) return;
        message = message.replaceAll("json:", "");
        final IChatBaseComponent component = IChatBaseComponent
                .ChatSerializer.a(message);
        ((CraftPlayer) player).getHandle()
                .playerConnection
                .sendPacket(new PacketPlayOutChat(component));
    }

    @Override
    public void sendTitle(Player player, String title, String subTitle) {
        PlayerConnection connection = ((CraftPlayer) player)
                .getHandle().playerConnection;
        PacketPlayOutTitle titleInfo = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.TIMES,
                null,
                20,
                60,
                20
        );
        connection.sendPacket(titleInfo);
        if (title != null) {
            IChatBaseComponent component = IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" +
                            title +
                            "\"}");
            connection.sendPacket(new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.TITLE,
                    component
            ));
        }

        if (subTitle != null) {
            IChatBaseComponent component = IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" +
                            subTitle +
                            "\"}");
            connection.sendPacket(new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                    component
            ));
        }
    }

    @Override
    public void sendActionBar(Player player, String message) {
        if (message == null) return;
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer
                .a("{\"text\": \"" +
                        message +
                        "\"}");
        PacketPlayOutChat chat = new PacketPlayOutChat(
                component, (byte) 2
        );
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }

    @Override
    public void sendParticle(Location location, String type, int offsetX, int offsetY, int offsetZ, int amount, int data) {
        if (type == null) return;
        EnumParticle particle = EnumParticle.valueOf(type);
        float x = (float) location.getBlockX();
        float y = (float) location.getBlockY();
        float z = (float) location.getBlockZ();
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particle, true, x, y, z, offsetX, offsetY, offsetZ, data, amount, 1);
        for (final Player player : location.getWorld().getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
        }
    }

    @Override
    public void setIt(Game game, Player it) {
        Map<UUID, List<FakeTeam>> teams = FakeTeamManager.PLAYER_TEAMS;
        for (FakeTeam team : teams.getOrDefault(it.getUniqueId(), new ArrayList<>())) {
            FakeTeamManager.reset(it, team);
        }
        teams.remove(it.getUniqueId());

        List<FakeTeam> fakeTeams = teams.getOrDefault(it.getUniqueId(), new ArrayList<>());
        FakeTeam itTeam = new FakeTeam(ChatColor.RED + "[IT] ",
                "",
                0
        );
        itTeam.addMember(it.getName());
        if (!fakeTeams.isEmpty()) {
            fakeTeams.clear();
        }
        fakeTeams.add(itTeam);
        teams.put(it.getUniqueId(), fakeTeams);

        FakeTeamManager.sendTeam(it, itTeam);

        for (TagPlayer player : game.getPlayers()) {
            player.getPlayer().ifPresent(bukkitPlayer -> {
                if (bukkitPlayer.getUniqueId().equals(it.getUniqueId())) return;
                FakeTeamManager.sendTeam(bukkitPlayer, itTeam);
            });
        }
    }

    @Override
    public void removeIt(Game game, Player it) {
        Map<UUID, List<FakeTeam>> teams = FakeTeamManager.PLAYER_TEAMS;
        for (FakeTeam team : teams.getOrDefault(it.getUniqueId(), new ArrayList<>())) {
            FakeTeamManager.reset(it, team);
        }
        teams.remove(it.getUniqueId());

        List<FakeTeam> fakeTeams = teams.getOrDefault(it.getUniqueId(), new ArrayList<>());
        FakeTeam survivorTeam = new FakeTeam(ChatColor.GREEN + "",
                "",
                1
        );
        survivorTeam.addMember(it.getName());
        if (!fakeTeams.isEmpty()) {
            fakeTeams.clear();
        }
        fakeTeams.add(survivorTeam);
        teams.put(it.getUniqueId(), fakeTeams);

        FakeTeamManager.sendTeam(it, survivorTeam);


        for (TagPlayer player : game.getPlayers()) {
            player.getPlayer().ifPresent(bukkitPlayer -> {
                if (bukkitPlayer.getUniqueId().equals(it.getUniqueId())) return;
                FakeTeamManager.sendTeam(bukkitPlayer, survivorTeam);
            });
        }
    }

    @Override
    public ChunkGenerator getGenerator() {
        return new ChunkGenerator() {
            @Override
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                return createChunkData(world);
            }
        };
    }
}
