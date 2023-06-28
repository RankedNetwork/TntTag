package ga.justreddy.wiki.tnttag.nms.v1_8_R3;

import ga.justreddy.wiki.tnttag.api.game.Game;
import ga.justreddy.wiki.tnttag.nms.Nms;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

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
        EntityPlayer itPlayer = ((CraftPlayer) it).getHandle();
        itPlayer.listName = CraftChatMessage.fromString("&c[IT] ")[0];
        game.getPlayers().forEach(tagPlayer ->  {
            if (!tagPlayer.getPlayer().isPresent()) return;
            Player player = tagPlayer.getPlayer().get();
            EntityPlayer survivorPlayer = ((CraftPlayer) player).getHandle();
            PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, itPlayer
            );
            survivorPlayer.playerConnection.sendPacket(info);

        });
    }

    @Override
    public void removeIt(Game game, Player it) {
        EntityPlayer itPlayer = ((CraftPlayer) it).getHandle();
        itPlayer.listName = CraftChatMessage.fromString(null)[0];
        game.getPlayers().forEach(tagPlayer ->  {
            if (!tagPlayer.getPlayer().isPresent()) return;
            Player player = tagPlayer.getPlayer().get();
            EntityPlayer survivorPlayer = ((CraftPlayer) player).getHandle();
            PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, itPlayer
            );
            survivorPlayer.playerConnection.sendPacket(info);
        });
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
