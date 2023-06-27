package ga.justreddy.wiki.tnttag.api.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Cuboid {

    Location highPoints;
    Location lowPoints;
    World world;
    int x1;
    int y1;
    int z1;
    int x2;
    int y2;
    int z2;

    public Cuboid(Location highPoints, Location lowPoints) {
        this.highPoints = highPoints;
        this.lowPoints = lowPoints;
        this.world = highPoints.getWorld();
        this.x1 = Math.min(highPoints.getBlockX(), lowPoints.getBlockX());
        this.y1 = Math.min(highPoints.getBlockY(), lowPoints.getBlockY());
        this.z1 = Math.min(highPoints.getBlockZ(), lowPoints.getBlockZ());
        this.x2 = Math.max(highPoints.getBlockX(), lowPoints.getBlockX());
        this.y2 = Math.max(highPoints.getBlockY(), lowPoints.getBlockY());
        this.z2 = Math.max(highPoints.getBlockZ(), lowPoints.getBlockZ());
        Location highPoints1 = new Location(this.world, this.x2, this.y2, this.z2);
        Location lowPoints1 = new Location(this.world, this.x1, this.y1, this.z1);
    }

    public boolean contains(Location location) {
        return location.getWorld() == world
                && location.getBlockX() >= x1
                && location.getBlockX() <= x2
                && location.getBlockY() <= y2
                && location.getBlockZ() >= z1
                && location.getBlockZ() <= z2;
    }

    public void clear() {
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                for (int z = z1; z < z2; z++) {
                    Block block = this.world.getBlockAt(x, y, z);
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR);
                    }
                }


            }

        }
    }
}
