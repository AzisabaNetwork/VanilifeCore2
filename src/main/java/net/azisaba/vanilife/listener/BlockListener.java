package net.azisaba.vanilife.listener;

import net.azisaba.vanilife.Vanilife;
import net.azisaba.vanilife.plot.Plot;
import net.azisaba.vanilife.user.User;
import net.azisaba.vanilife.util.Materials;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
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

        if (! event.isCancelled() && player.getGameMode() == GameMode.SURVIVAL && ! silktouch && Vanilife.random.nextDouble() < 0.4)
        {
            User user = User.getInstance(player);

            if (Materials.MINING.contains(block.getType()) && Vanilife.random.nextDouble() < (Materials.ORES.contains(block.getType()) ? 0.01 : 0.02))
            {
                user.setMola(user.getMola() + (Materials.ORES.contains(block.getType()) ? 2 : 1), "reward.category.mining", NamedTextColor.YELLOW);
            }

            if (Materials.LOGGING.contains(block.getType()) && Vanilife.random.nextDouble() < 0.01)
            {
                user.setMola(user.getMola() + 3, "reward.category.logging", NamedTextColor.YELLOW);
            }

            if (Materials.SEICHI.contains(block.getType()) && Vanilife.random.nextDouble() < 0.01)
            {
                user.setMola(user.getMola() + 4, "reward.category.seichi", NamedTextColor.YELLOW);
            }

            if (Materials.FARMING.contains(block.getType()) && Vanilife.random.nextDouble() < 0.01)
            {
                if ((block.getBlockData() instanceof Ageable ageable) && ageable.getAge() < ageable.getMaximumAge())
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
}
