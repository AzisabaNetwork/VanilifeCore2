package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.entity.GatewayEntity;
import net.azisaba.vanilife.vwm.VanilifeWorldManager;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EntityLightning;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class GatewayListener implements Listener
{
    private final Map<Location, ItemDisplay> gateways = new HashMap<>();

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        Item drop = event.getItemDrop();

        Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> {
            Block block = drop.getLocation().getBlock();

            if (this.gateways.entrySet().stream().anyMatch(gateway -> gateway.getKey().equals(block.getLocation())))
            {
                return;
            }

            if (! drop.getItemStack().isSimilar(Vanilife.getEnchantedRottenFlesh()) ||
                    block.getType() != Material.WATER_CAULDRON)
            {
                return;
            }

            Location location = block.getLocation();

            if (location.getWorld().equals(VanilifeWorldManager.getUnderworld()))
            {
                return;
            }

            drop.remove();
            block.setType(Material.CAULDRON);

            EntityLightning lightning = new EntityLightning(EntityTypes.am, ((CraftWorld) location.getWorld()).getHandle());
            PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(lightning, Vanilife.random.nextInt(0, 1000000), new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

            location.getWorld().getPlayers().forEach(player -> ((CraftPlayer) player).getHandle().c.sendPacket(packet));
            location.getWorld().playSound(location, Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.2f);

            ItemDisplay hologram = (ItemDisplay) new GatewayEntity(location.clone().add(0.5, 0.6, 0.5)).asEntity();

            this.gateways.put(location.getBlock().getLocation(), hologram);

            Bukkit.getScheduler().runTaskLater(Vanilife.getPlugin(), () -> {
                this.gateways.remove(location);
                hologram.remove();
            }, 20L * 60 * 3);
        }, 10L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Location location = event.getTo();

        if (this.gateways.keySet().stream().anyMatch(l -> l.equals(location.getBlock().getLocation())))
        {
            World underworld = VanilifeWorldManager.getUnderworld();
            Location access = new Location(underworld, location.getX(), underworld.getHighestBlockYAt(location.getBlockX(), location.getBlockZ()) + 1, location.getZ());
            event.setTo(access);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        new HashMap<>(this.gateways).entrySet().stream()
                .filter(gateway -> gateway.getKey().equals(event.getBlock().getLocation()))
                .forEach(gateway -> {
                    this.gateways.remove(gateway.getKey());
                    gateway.getValue().remove();
                });
    }
}
