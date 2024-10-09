package net.azisaba.vanilife.vwm;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.ui.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RandomTeleporter
{
    private final World world;
    private final int bound;
    private final List<Material> dangerous = new ArrayList<>();

    public RandomTeleporter(World world, int bound, Material... dangerous)
    {
        this.world = world;
        this.bound = bound;
        this.dangerous.addAll(List.of(dangerous));
    }

    public void teleport(Player player)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Location location = safeLocation(world);

                Bukkit.getScheduler().runTask(Vanilife.getPlugin(), () -> {
                    player.teleport(location);
                    player.sendMessage(Language.translate("cmd.rtp.teleported", player).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD));
                    player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.0f);
                });
            }
        }.runTaskAsynchronously(Vanilife.getPlugin());
    }

    public Location safeLocation(World world)
    {
        Location location = this.randomLocation(world);
        int i = 0;

        while (! this.isLocationSafe(location))
        {
            location = this.randomLocation(world);
            i ++;

            if (100 < i)
            {
                return world.getSpawnLocation();
            }
        }

        return location;
    }

    public Location randomLocation(World world)
    {
        Location randomLocation = new Location(world, Vanilife.random.nextInt(this.bound), 0, Vanilife.random.nextInt(this.bound));
        randomLocation.setY(randomLocation.getWorld().getHighestBlockYAt(randomLocation) + 1);
        return randomLocation;
    }

    private boolean isLocationSafe(Location location)
    {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        return ! this.dangerous.contains(block.getType()) && ! this.dangerous.contains(below.getType()) && below.getType().isSolid() && above.getType() == Material.AIR && Plot.getInstance(location.getChunk()) == null;
    }
}
