package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.housing.Housing;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Materials;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener
{
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        Plot plot = Plot.getInstance(block.getLocation().getChunk());

        if (plot != null)
        {
            plot.onBlockBreak(event);
        }

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        boolean silktouch = tool.getType() != Material.AIR && 0 < tool.getEnchantmentLevel(Enchantment.SILK_TOUCH);

        if (! event.isCancelled() && player.getGameMode() == GameMode.SURVIVAL && ! silktouch)
        {
            User user = User.getInstance(player);

            if (Materials.MINING.contains(block.getType()) && Vanilife.random.nextDouble() < (Materials.ORES.contains(block.getType()) ? 0.01 : 0.04))
            {
                user.setMola(user.getMola() + (Materials.ORES.contains(block.getType()) ? 2 : 1), "reward.category.mining", NamedTextColor.YELLOW);
            }

            if (Materials.LOGGING.contains(block.getType()) && Vanilife.random.nextDouble() < 0.05)
            {
                user.setMola(user.getMola() + 3, "reward.category.logging", NamedTextColor.YELLOW);
            }

            if (Materials.SEICHI.contains(block.getType()) && Vanilife.random.nextDouble() < 0.01)
            {
                user.setMola(user.getMola() + 4, "reward.category.seichi", NamedTextColor.YELLOW);
            }

            if (Materials.FARMING.contains(block.getType()) && Vanilife.random.nextDouble() < 0.07)
            {
                if (! (block.getState() instanceof Ageable ageable))
                {
                    return;
                }

                if (ageable.getAge() < ageable.getMaximumAge())
                {
                    return;
                }

                user.setMola(user.getMola() + 2, "reward.category.farming", NamedTextColor.YELLOW);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Plot plot = Plot.getInstance(event.getBlock().getLocation().getChunk());

        if (plot != null)
        {
            plot.onBlockPlace(event);
        }
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event)
    {
        Location location = event.getBlock().getLocation();

        if (! location.getWorld().equals(Housing.getWorld()) && Plot.getInstance(location.getChunk()) != null)
        {
            return;
        }

        event.setNewCurrent(0);
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event)
    {
        Location location = event.getInitiator().getLocation();

        if (location == null)
        {
            return;
        }

        if (! location.getWorld().equals(Housing.getWorld()) && Plot.getInstance(location.getChunk()) != null)
        {
            return;
        }

        event.setCancelled(event.isCancelled() || event.getInitiator().getType() == InventoryType.HOPPER);
    }
}
